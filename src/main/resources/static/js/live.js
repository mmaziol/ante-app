const activePlayersListElement = document.getElementById('active-players-list');
const activePlayersErrorElement = document.getElementById('active-players-error');

const createNewElement = (elementType, className, text) => {
    const element = document.createElement(elementType);
    element.className = className;
    element.textContent = text;

    return element;
}

async function getRuleset() {
    const url = '/api/game/ruleset';

    try {
        const response = await fetch(url);

        if (response.ok) {
            return response.json();
        } else {
            console.log(`${response.status}`);
        }
    } catch (error) {
        console.log(error);
    }
}

async function getLiveStats() {
    const url = '/api/game/live-stats';

    try {
        const response = await fetch(url);

        if (response.ok) {
            return response.json();
        } else {
            console.log(`${response.status}`);
        }
    } catch (error) {
        console.log(error);
    }
}

async function getPlayerNames() {
    const ruleset = await getRuleset();

    if (ruleset === undefined) {
        console.log(`ruleset is undefined`);
        return;
    }

    const playersList = ruleset.players;
    const playerNames = [];

    playersList.forEach((player) => {
        playerNames.push(player.name);
    });

    return playerNames;
}

async function showActivePlayers() {
    const players = await getPlayerNames();

    if (players === undefined)
    {
        const errorMessage = createNewElement('p', 'active-players-error', 'Couldn\'t load active players');
        activePlayersErrorElement.appendChild(errorMessage);
        return;
    }

    players.forEach(player => {
        const row = document.createElement('li');
        row.className = 'player-row';

        const name = createNewElement('span', 'player-name', player);
        const buyIns = createNewElement('span', 'player-buy-ins', 'Buy-ins: 1');
        const buyInButton = createNewElement('button', 'buy-in-player', 'Buy-in');
        buyInButton.type = 'button';
        const kickButton = createNewElement('button', 'kick-player', 'Kick');
        kickButton.type = 'button';

        row.appendChild(name);
        row.appendChild(buyIns);
        row.appendChild(buyInButton);
        row.appendChild(kickButton);

        activePlayersListElement.appendChild(row);
    });
}

document.addEventListener('DOMContentLoaded', () => {
    showActivePlayers();
});