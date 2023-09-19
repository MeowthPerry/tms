package ru.github.meperry.tms.backend.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.github.meperry.tms.backend.model.Group;
import ru.github.meperry.tms.backend.model.Match;
import ru.github.meperry.tms.backend.repository.GroupRepository;
import ru.github.meperry.tms.backend.security.model.User;

/**
 * @author Islam Khabibullin
 */
@Service
@RequiredArgsConstructor
public class GroupService {

  private final GroupRepository groupRepository;

  public List<Group> distributeByGroups(List<User> participants, int groupCount) {
    if (groupCount < 1 || participants.size() < groupCount * 2) {
      // TODO 16.08 сделать исключение более конкретным
      throw new IllegalArgumentException();
    }

    // создаем groupCount групп с пустыми списками пользователей
    List<Group> groups = IntStream.range(0, groupCount)
        .mapToObj(i -> {
          Group group = new Group();
          group.setParticipants(new ArrayList<>());
          return group;
        })
        .collect(Collectors.toList());

    // TODO 16.08 реализовать случайное распределение
    int i = 0;
    for (User participant : participants) {
      groups.get(i++).getParticipants().add(participant);
      if (i >= groupCount) {
        i = 0;
      }
    }

    return groups;
  }

  public List<Group> saveAll(List<Group> groups) {
    return groupRepository.saveAll(groups);
  }

  public Group save(Group oneGroup) {
    return groupRepository.save(oneGroup);
  }

  public List<User> getSortedByScoreParticipants(Group group) {
    ArrayList<User> participants = new ArrayList<>(group.getParticipants());

    Map<Long, ArrayList<Match>> matchesByUserId = participants.stream().collect(Collectors.toMap(
        User::getUserId,
        participant -> new ArrayList<>()
    ));

    group.getMatches().forEach(match -> {
      matchesByUserId.get(match.getParticipantOne().getUserId()).add(match);
      if (match.getParticipantTwo() != null) {
        matchesByUserId.get(match.getParticipantTwo().getUserId()).add(match);
      }
    });

    participants.sort((user1, user2) -> Integer.compare(
        calculateRating(user1, matchesByUserId.get(user1.getUserId())),
        calculateRating(user2, matchesByUserId.get(user2.getUserId()))
    ));

    return participants;
  }

  private int calculateRating(User user, List<Match> matches) {
    return matches.stream().mapToInt(match -> {
      if (match.winner().getUserId().equals(user.getUserId())) {
        return 2;
      }
      else if (match.getParticipantTwo() != null
               && match.getParticipantOneScore().equals(match.getParticipantTwoScore())) {
        return 1;
      }
      return 0;
    }).sum();
  }
}
