# Ante - Custom Poker Rules

A small web app for running home poker tournaments. The host sets up the game once (players, buy-in, chips, blind structure, payouts), and during play the screen works as a simple table dashboard: who is still in, buy-ins, the pot, the current blinds and the time until they go up.

> **Status:** work in progress. Tournament ("points") mode only. Cash game mode is planned for later.

## How a game works

1. **Start page** (`index.html`): the entry point.
2. **Setup** (`setup.html`): the host configures the game:
    - buy-in amount and currency
    - player names
    - chip colors and values, and the starting stack per player
    - starting small blind and how many minutes each blind level lasts
    - the blind ladder: number of levels (default 8, max 16) and the increase between each level (+33%, +50% or +100%)
    - payouts per place (percentages must add up to 100)
3. **Live** (`live.html`): the game is running:
    - active players with their buy-in count, plus Buy-in and Kick buttons
    - eliminated players with a Re-buy button (puts the player back in and counts one more buy-in)
    - current blinds, pot and blind timer
4. **Results** (`results.html`): final standings and payouts.

The big blind is always twice the small blind. The ruleset is checked once when the game starts and stays locked for the whole game.

## Tech stack

| Part | Technology |
| :--- | :--- |
| Backend | Java 25, Spring Boot 4.1 (Spring Web MVC), Maven |
| Frontend | Plain HTML, CSS and JavaScript, served by Spring Boot from `src/main/resources/static/` |
| Formatting | Prettier (frontend files only) |
| Database | MySQL with Spring Data JPA, planned. Not connected yet, the game currently lives in memory |
| Deployment | Docker, planned |

## Running it locally

You need **Java 25**. Maven doesn't need to be installed, the project comes with the Maven wrapper.

```bash
# Windows
mvnw.cmd spring-boot:run

# macOS / Linux
./mvnw spring-boot:run
```

Then open <http://localhost:8080>.

The game is kept in memory, so restarting the server clears the current game.

### Formatting the frontend (optional)

Prettier needs Node.js and is only used during development.

```bash
npm install
npm run format        # format all files in src/main/resources/static
npm run format:check  # only check, don't change anything
```

## Project structure

```
src/main/java/com/mmaziol/ante/
├── AnteApplication.java   # Spring Boot entry point
├── controller/            # REST endpoints (GameController)
├── service/               # GameService, holds the current game
├── model/                 # game rules and state: Ruleset, ActiveGame, Ladder, Blind, Chip, Payout, Money, Player
└── dto/                   # what the API sends back: GameLiveStats, GameOverview, ...

src/main/resources/static/
├── index.html / setup.html / live.html / results.html
├── css/
└── js/
```

## API

All endpoints are under `/api/game`.

| Method | Endpoint | What it does |
| :--- | :--- | :--- |
| `POST` | `/start` | Starts a new game from the ruleset sent by the setup page |
| `GET` | `/ruleset` | The ruleset of the current game |
| `GET` | `/live-stats` | Live state: blinds, timer, pot, active and eliminated players, buy-in counts |
| `GET` | `/overview` | Summary of the game, used for the results page |
| `POST` | `/finish` | Ends the current game |
| `POST` | `/buy-in/{playerName}` | Adds one buy-in for a player |
| `POST` | `/eliminate/{playerName}` | Kicks a player out (the last player standing can't be kicked) |
| `POST` | `/rebuy/{playerName}` | Puts an eliminated player back in and adds one buy-in |
| `POST` | `/remove-from-eliminated/{playerName}` | Puts an eliminated player back in for free (undo a kick) |
| `POST` | `/timer/stop` | Pauses the blind timer |
| `POST` | `/timer/resume` | Resumes the blind timer |
