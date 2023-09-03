# Tournament Management System - Telegram Bot

This is a telegram bot component of the Tournament Management System.

## Usage

### Commands

#### Private chat:
* `/start`: Begin interacting with the bot;
* `/create_tournament`: Initiate the process of creating a new tournament;
* `/register <tournament_id>`: Register for a specific tournament using its ID;
* `/start_tournament`: Starts the tournament if it was created by you
* `/my_tournaments`: Returns a list of tournaments that you have created or are participating in;
* `/my_matches`: Returns a list of your upcoming matches
* `/help`: Display the list of available commands and their descriptions.

#### Group chat (`@tournament_manager_bot <message>`):
* `/choose_tournament <tournament_id>`: Selects the tournament in the context of which the following teams will be executed and whose news will be published in this chat;
* `/report_match <participant_one> <participant_one_score> <participant_two> <participant_two_score>`: Report the result of a match.
