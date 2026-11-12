const activePlayersListElement = document.getElementById('active-players-list');

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