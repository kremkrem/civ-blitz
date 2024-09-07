import {Button, Header, Icon, Input, Table} from "semantic-ui-react";
import React from 'react';
import PlayerAvatar from "../player/PlayerAvatar";
import axios from "axios";
import {useHistory} from "react-router-dom";


const MatchLeaderboard = ({match, leaderboard, loggedInPlayer, leaderboardChanged}) => {

    const tableRows = [];
    const currentPlayerSignup = match.signups && match.signups.find(s => s.playerId === loggedInPlayer.discordId);
    const isEligibleAdmin = loggedInPlayer.isAdmin && (loggedInPlayer.isSuperAdmin || !currentPlayerSignup);
    const canBeCompleted = match.matchState === 'POST_MATCH' && isEligibleAdmin;

    const updateScore = (playerId, score) => {
        const newLeaderboard = {...leaderboard}
        if (!Number.isInteger(newLeaderboard[playerId]) && ('objectiveScore' in newLeaderboard[playerId])) {
            const newScore = {...newLeaderboard[playerId]}
            newScore.finalScore = Number(score);
            newLeaderboard[playerId] = newScore;
        } else {
            newLeaderboard[playerId] = Number(score);
        }
        leaderboardChanged(newLeaderboard);
    }

    for (const [playerId, score] of Object.entries(leaderboard)) {
        const signup = match.signups.find(signup => signup.playerId === playerId);
        const stars = [];
        let finalScore = 0;
        if (!Number.isInteger(score) && ('objectiveScore' in score)) {
            finalScore = ('finalScore' in score && score.finalScore != null)
                ? score.finalScore : score.objectiveScore;
            const yellowStarCount = Math.min(score.objectiveScore, finalScore);

            for (let starNum = 0; starNum < yellowStarCount; starNum++) {
                stars.push(<Icon size='large' color='yellow' name='star'/>);
            }
            for (let starNum = score.objectiveScore; starNum < finalScore; starNum++) {
                stars.push(<Icon size='large' color='blue' name='star'/>);
            }
            for (let starNum = finalScore; starNum < score.objectiveScore; starNum++) {
                stars.push(<Icon size='large' disabled color='red' name='star outline'/>)
            }
        } else {
            finalScore = score;
            for (let starNum = 0; starNum < score; starNum++) {
                stars.push(<Icon size='large' color='yellow' name='star'/>);
            }
        }

        tableRows.push(<Table.Row key={playerId}>
            <Table.Cell>
                <Header as='h4' image>
                    <PlayerAvatar player={signup.player}/>
                    <Header.Content>
                        {signup.player.discordUsername}
                    </Header.Content>
                </Header>
            </Table.Cell>
            <Table.Cell>{stars}</Table.Cell>
            {canBeCompleted &&
                <Table.Cell>
                    <Input type='number' value={finalScore}
                           onChange={(event, data) => updateScore(playerId, data.value)}/>
                </Table.Cell>
            }
        </Table.Row>);
    }


    const history = useHistory();

    const completeMatch = () => {
        const uploadableLeaderboard = {}
        for (const [playerId, score] of Object.entries(leaderboard)) {
            if (Number.isInteger(score)) {
                uploadableLeaderboard[playerId] = score;
            } else if ('finalScore' in score) {
                uploadableLeaderboard[playerId] = score.finalScore;
            } else {
                uploadableLeaderboard[playerId] = score.objectiveScore;
            }
        }
        axios.put('/api/matches/'+match.matchId+'/COMPLETED', uploadableLeaderboard)
            .then(response => {
                history.push("/matches");
            })
            .catch(console.error)
    }

    return (
        <React.Fragment>
            <Table basic='very' celled collapsing>
                {canBeCompleted &&
                <Table.Header>
                    <Table.HeaderCell>Player</Table.HeaderCell>
                    <Table.HeaderCell>Stars claimed from objectives</Table.HeaderCell>
                    <Table.HeaderCell>Confirm final score</Table.HeaderCell>
                </Table.Header>
                }
                <Table.Body>
                    {tableRows}
                </Table.Body>
            </Table>
            {canBeCompleted &&
            <Button color='green' onClick={completeMatch}>Finalise match and award stars</Button>
            }
        </React.Fragment>
    );
};

export default MatchLeaderboard;