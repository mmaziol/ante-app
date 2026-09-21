document.addEventListener('DOMContentLoaded', () => {
    regenerateLevelRows();
});

const playersList = document.getElementById('players-list');
const addPlayerButton = document.getElementById('add-player');

addPlayerButton.addEventListener('click', () => {
    const row = document.createElement('li');
    row.className = 'player-row';
    row.innerHTML = `
        <input type="text" class="player-name" placeholder="Player name">
        <button type="button" class="remove-player">X</button>
    `;
    playersList.appendChild(row);
});

playersList.addEventListener('click', (event) => {
    if (event.target.classList.contains('remove-player')) {
        event.target.closest('.player-row').remove();
    }
});

const chipsList = document.getElementById('chips-list');
const addChipButton = document.getElementById('add-chip');

addChipButton.addEventListener("click", () => {
    const row = document.createElement('li');
    row.className = 'chip-row';
    row.innerHTML = `
        <input type="text" class="chip-color" placeholder="Color">
        <input type="number" class="chip-value" placeholder="Value" min="5" step="5">
        <button type="button" class="remove-chip">X</button>
    `;
    chipsList.appendChild(row);
});

chipsList.addEventListener('click', (event) => {
    if (event.target.classList.contains('remove-chip')) {
        event.target.closest('.chip-row').remove();
    }
});

const payoutsList = document.getElementById('payouts-list');
const addPayoutButton = document.getElementById('add-payout');

addPayoutButton.addEventListener('click', () => {
    const row = document.createElement('li');
    row.className = 'payout-row';
    row.innerHTML = `
        <label>Place <input type="number" class="payout-place" min="1"></label>
        <label>Percentage <input type="number" class="payout-percentage" min="1" max="100"></label>
        <button type="button" class="remove-payout">X</button>
    `;
    payoutsList.appendChild(row);
});

payoutsList.addEventListener('click', (event) => {
    if (event.target.classList.contains('remove-payout')) {
        event.target.closest('.payout-row').remove();
    }
});

const levelsList = document.getElementById('level-increases-list');
const levelCount = document.getElementById(`level-count`);
const overtimeIncreaseText = document.getElementById('overtime-increase-text')

levelCount.addEventListener('input', () => {
    regenerateLevelRows();
});

const addLevelRow = (levelNumber) => {
    const row = document.createElement('tr');
    row.className = 'level-increase-row';
    row.innerHTML = `
            <td class="level-label">${levelNumber}</td>
            <td class="level-small-blind">-</td>
            <td class="level-big-blind">-</td>
            <td>
                <select class="level-increase-select">
                    <option value="QUARTER">+25%</option>
                    <option value="THIRD">+33%</option>
                    <option value="HALF">+50%</option>
                    <option value="DOUBLE">+100%</option>
                </select>
            </td>
    `;
    levelsList.appendChild(row);
};

const deleteLevelRows = () => {
    levelsList.replaceChildren();
}

const swapOvertimeIncreaseText = (levelNumber) => {
    overtimeIncreaseText.textContent = `Overtime increase (level ${levelNumber}+)`;
}

const regenerateLevelRows = () => {
    deleteLevelRows();
    const count = Number(levelCount.value);

    for (let i = 2; i <= count; i++) {
        addLevelRow(i);
    }

    swapOvertimeIncreaseText(count + 1);
}