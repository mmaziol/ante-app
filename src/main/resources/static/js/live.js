const activePlayersListElement = document.getElementById('active-players-list');
const activePlayersErrorElement = document.getElementById('active-players-error');

const createNewElement = (elementType, className, text) => {
    const element = document.createElement(elementType);
    element.className = className;
    element.textContent = text;

    return element;
};

activePlayersListElement.addEventListener('click', async (event) => {
    if (event.target.classList.contains('buy-in-player')) {
        const nameElement = event.target.closest('.player-row').querySelector('.player-name');
        await sendPlayerAction('buy-in', nameElement.textContent);
    } else if (event.target.classList.contains('kick-player')) {
        const nameElement = event.target.closest('.player-row').querySelector('.player-name');
        await sendPlayerAction('eliminate', nameElement.textContent);
    }
});

async function sendPlayerAction(action, playerName) {
    const name = encodeURIComponent(playerName);
    const url = `/api/game/${action}/${name}`;
    const options = {
        method: 'POST',
    };

    try {
        const response = await fetch(url, options);

        if (response.ok) {
            await showActivePlayers();
        } else {
            console.log(`${response.status}`);
        }
    } catch (error) {
        console.log(error);
    }
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

async function getGameLiveStats() {
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

const getPlayerBuyInCount = (buyIns, playerName) => {
    const match = buyIns.find((element) => element.playerName === playerName);

    return match.buyInCount;
};

async function showActivePlayers() {
    const liveStats = await getGameLiveStats();

    if (liveStats === undefined) {
        console.log(`live stats are undefined`);
        return;
    }

    const players = liveStats.activePlayers;
    const buyInCounts = liveStats.buyInCounts;

    activePlayersListElement.replaceChildren();

    players.forEach((player) => {
        const row = document.createElement('li');
        row.className = 'player-row';

        const name = createNewElement('span', 'player-name', player);
        const buyIns = createNewElement(
            'span',
            'player-buy-ins',
            `Buy-ins: ${getPlayerBuyInCount(buyInCounts, player)}`,
        );
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
