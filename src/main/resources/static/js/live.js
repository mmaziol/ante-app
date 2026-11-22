const activePlayersListElement = document.getElementById('active-players-list');
const eliminatedPlayersListElement = document.getElementById('eliminated-players-list');
const currentPotElement = document.getElementById('current-pot');
const currentSmallBlindElement = document.getElementById('current-small-blind');
const currentBigBlindElement = document.getElementById('current-big-blind');
const chipsListElement = document.getElementById('chips-list');
const potCurrencyElement = document.getElementById('pot-currency');

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

eliminatedPlayersListElement.addEventListener('click', async (event) => {
    if (event.target.classList.contains('rebuy-player')) {
        const nameElement = event.target.closest('.player-row').querySelector('.player-name');
        await sendPlayerAction('rebuy', nameElement.textContent);
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
            await refreshLivePage();
        } else {
            console.log(`${response.status}`);
        }
    } catch (error) {
        console.log(error);
    }
}

async function fetchJson(endpoint) {
    const url = `/api/game/${endpoint}`;

    try {
        const response = await fetch(url);

        if (response.ok) {
            return await response.json();
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

const showActivePlayers = (players, buyInCounts) => {
    activePlayersListElement.replaceChildren();

    players.forEach((player) => {
        const row = generatePlayerRow(player, getPlayerBuyInCount(buyInCounts, player));
        const buyInButton = createNewElement('button', 'buy-in-player', 'Buy-in');
        buyInButton.type = 'button';
        const kickButton = createNewElement('button', 'kick-player', 'Kick');
        kickButton.type = 'button';

        row.appendChild(buyInButton);
        row.appendChild(kickButton);

        activePlayersListElement.appendChild(row);
    });
};

const showEliminatedPlayers = (players, buyInCounts) => {
    eliminatedPlayersListElement.replaceChildren();

    players.forEach((player) => {
        const row = generatePlayerRow(player, getPlayerBuyInCount(buyInCounts, player));
        const rebuyButton = createNewElement('button', 'rebuy-player', 'Re-buy');
        rebuyButton.type = 'button';

        row.classList.add('eliminated-player-row');
        row.appendChild(rebuyButton);

        eliminatedPlayersListElement.appendChild(row);
    });
};

const generatePlayerRow = (playerName, buyInCount) => {
    const row = document.createElement('li');
    row.className = 'player-row';

    const name = createNewElement('span', 'player-name', playerName);
    const buyIns = createNewElement('span', 'player-buy-ins', `Buy-ins: ${buyInCount}`);

    row.appendChild(name);
    row.appendChild(buyIns);

    return row;
};

const showChips = (chips) => {
    chipsListElement.replaceChildren();

    chips.forEach((chip) => {
        const row = generateChipRow(chip.color, chip.value);

        chipsListElement.appendChild(row);
    });
};

const generateChipRow = (color, value) => {
    const row = document.createElement('li');
    row.className = 'chip-row';

    const chipColor = createNewElement('span', 'chip-icon', '');
    chipColor.dataset.color = color;
    const chipValue = createNewElement('span', 'chip-value', value);

    row.appendChild(chipColor);
    row.appendChild(chipValue);

    return row;
};

const showValue = (element, value) => {
    element.textContent = `${value}`;
};

async function loadRuleset() {
    const ruleset = await fetchJson('ruleset');

    if (ruleset === undefined) {
        console.log('ruleset is undefined');
        return;
    }

    const currency = ruleset.money.currency;
    const chips = ruleset.chips;

    showChips(chips);
    showValue(potCurrencyElement, currency);
}

async function refreshLivePage() {
    const liveStats = await fetchJson('live-stats');

    if (liveStats === undefined) {
        console.log('liveStats is undefined');
        return;
    }

    const activePlayers = liveStats.activePlayers;
    const eliminatedPlayers = liveStats.eliminatedPlayers;
    const buyInCounts = liveStats.buyInCounts;
    const currentPot = liveStats.totalPot;
    const currentSmallBlind = liveStats.currentSmallBlind;
    const currentBigBlind = liveStats.currentBigBlind;

    showActivePlayers(activePlayers, buyInCounts);
    showEliminatedPlayers(eliminatedPlayers, buyInCounts);
    showValue(currentPotElement, currentPot);
    showValue(currentSmallBlindElement, currentSmallBlind);
    showValue(currentBigBlindElement, currentBigBlind);
}

document.addEventListener('DOMContentLoaded', async () => {
    await loadRuleset();
    await refreshLivePage();
});
