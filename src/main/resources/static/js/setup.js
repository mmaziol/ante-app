const playersListElement = document.getElementById('players-list');
const addPlayerElement = document.getElementById('add-player');

addPlayerElement.addEventListener('click', () => {
    const row = document.createElement('li');
    row.className = 'player-row';
    row.innerHTML = `
        <input type="text" class="player-name" placeholder="Player name">
        <button type="button" class="remove-player">X</button>
    `;
    playersListElement.appendChild(row);
});

playersListElement.addEventListener('click', (event) => {
    if (event.target.classList.contains('remove-player')) {
        event.target.closest('.player-row').remove();
    }
});

const chipsListElement = document.getElementById('chips-list');
const addChipElement = document.getElementById('add-chip');

addChipElement.addEventListener('click', () => {
    const row = document.createElement('li');
    row.className = 'chip-row';
    row.innerHTML = `
        <input type="text" class="chip-color" placeholder="Color">
        <input type="number" class="chip-value" placeholder="Value" min="5" step="5">
        <button type="button" class="remove-chip">X</button>
    `;
    chipsListElement.appendChild(row);
});

chipsListElement.addEventListener('click', (event) => {
    if (event.target.classList.contains('remove-chip')) {
        event.target.closest('.chip-row').remove();
    }
});

const payoutsListElement = document.getElementById('payouts-list');
const addPayoutElement = document.getElementById('add-payout');

addPayoutElement.addEventListener('click', () => {
    const row = document.createElement('li');
    row.className = 'payout-row';
    row.innerHTML = `
        <label>Place <input type="number" class="payout-place" min="1"></label>
        <label>Percentage <input type="number" class="payout-percentage" min="1" max="100">%</label>
        <button type="button" class="remove-payout">X</button>
    `;
    payoutsListElement.appendChild(row);
});

payoutsListElement.addEventListener('click', (event) => {
    if (event.target.classList.contains('remove-payout')) {
        event.target.closest('.payout-row').remove();
    }
});

const levelsListElement = document.getElementById('level-increases-list');
const levelCountElement = document.getElementById('level-count');
const overtimeIncreaseElement = document.getElementById('overtime-increase');
const overtimeIncreaseTextElement = document.getElementById('overtime-increase-text');
const startingSmallBlindElement = document.getElementById('small-blind-value');
const blindIncreaseTimeInMinutesElement = document.getElementById('blind-increase-minutes');
const levelOneSmallBlindElement = document.getElementById('level-one-small');
const levelOneBigBlindElement = document.getElementById('level-one-big');
const buyInValueElement = document.getElementById('buy-in-value');
const currencyElement = document.getElementById('currency');
const chipsValuePerPlayerElement = document.getElementById('chips-value-per-player');
const setupFormElement = document.getElementById('setup-form');

const INCREASE_PERCENTAGES = {
    THIRD: 33,
    HALF: 50,
    DOUBLE: 100,
};

const swapLevelOneBlindsText = (smallBlindValue) => {
    if (startingSmallBlindElement.value !== "" && !(smallBlindValue % 5)) {
        levelOneSmallBlindElement.textContent = `${smallBlindValue}`;
        levelOneBigBlindElement.textContent = `${smallBlindValue * 2}`;
    } else {
        levelOneSmallBlindElement.textContent = `-`;
        levelOneBigBlindElement.textContent = `-`;
    }
}

const addLevelRow = (levelNumber) => {
    const row = document.createElement('tr');
    row.className = 'level-increase-row';
    row.innerHTML = `
            <td class="level-label">${levelNumber}</td>
            <td class="level-small-blind">-</td>
            <td class="level-big-blind">-</td>
            <td>
                <select class="level-increase-select" data-level="${levelNumber}" >
                    <option value="THIRD">+33%</option>
                    <option value="HALF">+50%</option>
                    <option value="DOUBLE" selected>+100%</option>
                </select>
            </td>
    `;
    levelsListElement.appendChild(row);
};

const deleteLevelRows = () => {
    levelsListElement.replaceChildren();
};

const swapOvertimeIncreaseText = (levelNumber) => {
    overtimeIncreaseTextElement.textContent = `Overtime increase (level ${levelNumber}+)`;
};

const regenerateLevelRows = () => {
    deleteLevelRows();
    const count = Number(levelCountElement.value);

    for (let i = 2; i <= count; i++) {
        addLevelRow(i);
    }

    swapOvertimeIncreaseText(count + 1);
};

const recalculateLevels = () => {
    let smallBlindValue = Number(startingSmallBlindElement.value);
    const rows = levelsListElement.querySelectorAll('.level-increase-row');

    if (startingSmallBlindElement.value !== "" && !(smallBlindValue % 5)) {
        rows.forEach((row) => {
            let smallBlindRow = row.querySelector('.level-small-blind');
            let bigBlindRow = row.querySelector('.level-big-blind');
            let increaseString = row.querySelector('.level-increase-select').value;

            smallBlindValue = getMultipliedSmallBlind(smallBlindValue, increaseString);
            let bigBlindValue = smallBlindValue * 2;

            smallBlindRow.textContent = smallBlindValue;
            bigBlindRow.textContent = String(bigBlindValue);
        });
    } else {
        rows.forEach((row) => {
            let smallBlindRow = row.querySelector('.level-small-blind');
            let bigBlindRow = row.querySelector('.level-big-blind');
            smallBlindRow.textContent = "-";
            bigBlindRow.textContent = "-";
        });
    }
};

const getMultipliedSmallBlind = (smallBlindValue, increasePercentageKey)  => {
    const multiplier = (100 + INCREASE_PERCENTAGES[increasePercentageKey]) / 100;

    return (Math.round((smallBlindValue * multiplier) / 5)) * 5;
};

const getPlayers = () => {
    const playerList = [];
    const rows = playersListElement.querySelectorAll('.player-row');

    rows.forEach((row) => {
        const name = row.querySelector('.player-name').value;
        playerList.push({name});
    });

    return playerList;
};

const getChips = () => {
    const chipList = [];
    const rows = chipsListElement.querySelectorAll('.chip-row');

    rows.forEach((row) => {
        const color = row.querySelector('.chip-color').value;
        const value = Number(row.querySelector('.chip-value').value);
        
        chipList.push({color, value});
    });

    return chipList;
};

const getPayouts = () => {
    const payoutList = [];
    const rows = payoutsListElement.querySelectorAll('.payout-row');

    rows.forEach((row) => {
        const place = Number(row.querySelector('.payout-place').value);
        const percentageValue = Number(row.querySelector('.payout-percentage').value);

        payoutList.push({place, percentageValue});
    });

    return payoutList;
};

const getBlind = () => {
    const smallBlindValue = Number(startingSmallBlindElement.value);
    const blindIncreaseTimeInMinutes = Number(blindIncreaseTimeInMinutesElement.value);

    return {smallBlindValue, blindIncreaseTimeInMinutes};
};

const getLadder = () => {
    const levelIncreases = [];
    const levelCount = Number(levelCountElement.value);
    const overtimeIncrease = overtimeIncreaseElement.value;

    const rows = levelsListElement.querySelectorAll('.level-increase-row');

    rows.forEach((row) => {
        const increaseValue = row.querySelector('.level-increase-select').value;
        levelIncreases.push(increaseValue);
    });

    return {levelCount, overtimeIncrease, levelIncreases};
};

const getMoney = () => {
    const buyInValue = Number(buyInValueElement.value);
    const currency = currencyElement.value;

    return {buyInValue, currency};
};

const getChipsValuePerPlayer = () => {
    return Number(chipsValuePerPlayerElement.value);
};

const validatePlayers = (players) => {
    const errorSet = new Set();
    const playersSet = new Set();

    players.forEach((player) => {
        if (player.name.trim() !== "") {
            playersSet.add(player.name.toLowerCase().trim());
        }
    });

    if (players.length < 2) {
        errorSet.add("There need to be at least 2 players!");
    }

    if (playersSet.size !== players.length) {
        errorSet.add("All player names must be unique and not empty!");
    }

    return errorSet;
};

const validateChips = (chips) => {
    const errorSet = new Set();

    chips.forEach((chip) => {
        if (chip.color.trim() === "") {
            errorSet.add("Chip colors cannot be empty!");
        }

        if (chip.value <= 0 || chip.value % 5 !== 0) {
            errorSet.add("Invalid chip values!");
        }
    });

    if (chips.length < 1) {
        errorSet.add("There must be at least one available chip!");
    }

    return errorSet;
};

const validatePayouts = (payouts) => {
    const errorSet = new Set();
    let totalPercentage = 0;

    payouts.forEach((payout) => {
        if (payout.percentageValue <= 0) {
            errorSet.add("Percentage cannot be zero or negative!");
        }

        totalPercentage += payout.percentageValue;
    });

    if (totalPercentage !== 100) {
        errorSet.add("Percentage must add up to 100%!");
    }

    return errorSet;
};

const validateBlind = (blind) => {
    const errorSet = new Set();

    if (blind.smallBlindValue <= 0 || blind.smallBlindValue % 5 !== 0) {
        errorSet.add("Invalid starting small blind value!");
    }

    if (blind.blindIncreaseTimeInMinutes <= 0) {
        errorSet.add("Blind increase interval cannot equal zero or be negative!");
    }

    return errorSet;
};

const validateLadder = (ladder) => {
    const errorSet = new Set();

    if (ladder.levelCount <= 1 || ladder.levelCount > 16) {
        errorSet.add("Number of levels has to be between 2 and 16!");
    }

    ladder.levelIncreases.forEach((increase) => {
        if (!Object.hasOwn(INCREASE_PERCENTAGES, increase)) {
            errorSet.add("Invalid percentage in levels!");
        }
    });

    if (!Object.hasOwn(INCREASE_PERCENTAGES, ladder.overtimeIncrease)) {
        errorSet.add("Invalid overtime increase!");
    }

    return errorSet;
};

const validateChipsValuePerPlayer = (chipsValuePerPlayer) => {
    const errorSet = new Set();

    if (chipsValuePerPlayer <= 0) {
        errorSet.add("Starting stack cannot be zero or negative!");
    }

    if (chipsValuePerPlayer % 5 !== 0) {
        errorSet.add("Starting stack needs to be divisible by 5!");
    }

    return errorSet;
};

const validateMoney = (money) => {
    const errorSet = new Set();

    if (money.buyInValue <= 0) {
        errorSet.add("Buy-in value cannot be zero or negative!");
    }

    if (money.currency.trim() === "") {
        errorSet.add("Invalid currency!");
    }

    return errorSet;
};

const validateRuleset = (ruleset) => {
    return [
        ...validatePlayers(ruleset.players),
        ...validateChips(ruleset.chips),
        ...validatePayouts(ruleset.payouts),
        ...validateBlind(ruleset.blind),
        ...validateLadder(ruleset.ladder),
        ...validateChipsValuePerPlayer(ruleset.chipsValuePerPlayer),
        ...validateMoney(ruleset.money)
    ];
};

const getRuleset = () => {
    const players = getPlayers();
    const chips = getChips();
    const payouts = getPayouts();
    const blind = getBlind();
    const ladder = getLadder();
    const money = getMoney();
    const chipsValuePerPlayer = getChipsValuePerPlayer();

    return {players, chips, payouts, blind, ladder, money, chipsValuePerPlayer};
};

levelCountElement.addEventListener('input', () => {
    regenerateLevelRows();
    recalculateLevels();
});

levelsListElement.addEventListener('change', (event) =>{
    if (event.target.classList.contains('level-increase-select')) {
        recalculateLevels();
    }
});

startingSmallBlindElement.addEventListener('input', () => {
    swapLevelOneBlindsText(Number(startingSmallBlindElement.value));
    recalculateLevels();
});

setupFormElement.addEventListener('submit', (event) =>{
    event.preventDefault();
    const ruleset = getRuleset();
    const rulesetErrors = validateRuleset(ruleset);

    if (rulesetErrors.length === 0) {
        console.log(ruleset);
    } else {
        rulesetErrors.forEach((error) => {
            console.log(error);
        });
    }
});

document.addEventListener('DOMContentLoaded', () => {
    swapLevelOneBlindsText(Number(startingSmallBlindElement.value));
    regenerateLevelRows();
    recalculateLevels();
});
