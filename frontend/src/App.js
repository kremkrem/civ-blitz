import './App.css';
import TopLevelMenu from "./header/TopLevelMenu";
import React, {useEffect, useState} from "react";
import CardStore from "./cards/CardStore";
import ModTester from "./ModTester";
import SingleDraft from "./SingleDraft";
import PlayerCollection from "./PlayerCollection";
import jwt from "jsonwebtoken";
import {Route, Switch, withRouter} from "react-router-dom";
import axios from 'axios';
import Footer from "./header/Footer";
import AdminPage from "./admin/AdminPage";
import MatchesPage from "./matches/MatchesPage";
import HomePage from "./HomePage";
import MatchPage from "./matches/MatchPage";
import PackOpener from "./cards/PackOpener";
import PackShopPage from "./cards/PackShopPage";
import DlcSettingsPage from "./player/DlcSettingsPage";
import PlayerLeaderboard from "./player/PlayerLeaderboard";
import KnownBugs from "./KnownBugs";

const App = ({location, history}) => {

    const [loading, setLoading] = useState(true);
    const [loggedInPlayer, setLoggedInPlayer] = useState();
    const [multiplayerEnabled, setMultiplayerEnabled] = useState(false);
    const [discordClientId, setDiscordClientId] = useState("");

    useEffect(() => {
        const urlParams = new URLSearchParams(location.search);
        let jsonWebToken = urlParams.get('token');
        if (jsonWebToken) {
            window.localStorage.setItem('token', jsonWebToken);
            history.replace({search: ''});
        } else {
            jsonWebToken = window.localStorage.getItem('token');
            // TODO check for expiry and trigger refresh of token here
        }

        if (jsonWebToken) {
            axios.defaults.headers.common['Authorization'] = jsonWebToken;
            const decoded = jwt.decode(jsonWebToken);
            setLoggedInPlayer({
                discordUsername: decoded.username,
                discordId: decoded.sub,
                discordAvatar: decoded.avatar,
                isAdmin: decoded.is_admin,
                isSuperAdmin: decoded.is_super_admin
            });
        }


        axios.get("/api/cards")
            .then((response) => {
                if (!CardStore.initialised) {
                    CardStore.addCards(response.data);
                    axios.get("/api/login/discord-client-id")
                        .then((response) => {
                            setDiscordClientId(response.data)
                        })
                        .catch((error) => {
                            console.error('Error loading Discord client id', error)
                        });
                    axios.get("/api/features")
                        .then((response) => {
                            setMultiplayerEnabled(response.data.multiplayer);
                            setLoading(false);
                        })
                        .catch((error) => {
                            console.error('Error loading feature flags', error);
                        });
                }
            })
            .catch((error) => {
                console.error('Error loading cards', error);
            });

    }, [history, location]);

    const logout = () => {
        window.localStorage.clear();
        setLoggedInPlayer(undefined);
        history.push('/');
    }

    if (loading) {
        return <div>Loading...</div>
    }
    return (
        <div>
            <TopLevelMenu loggedInPlayer={loggedInPlayer} multiplayerEnabled={multiplayerEnabled} discordClientId={discordClientId}/>

            <Switch>
                <Route exact path="/">
                    <HomePage multiplayerEnabled={multiplayerEnabled} />
                </Route>
                <Route exact path="/collection">
                    <PlayerCollection loggedInPlayer={loggedInPlayer}/>
                </Route>
                <Route exact path="/pack-shop">
                    <PackShopPage loggedInPlayer={loggedInPlayer}/>
                </Route>
                <Route path="/packs">
                    <PackOpener/>
                </Route>
                <Route path="/leaderboard">
                    <PlayerLeaderboard />
                </Route>
                <Route path="/modtester">
                    <ModTester/>
                </Route>
                <Route path="/single-draft">
                    <SingleDraft loggedInPlayer={loggedInPlayer}/>
                </Route>
                <Route exact path="/matches">
                    <MatchesPage loggedInPlayer={loggedInPlayer} />
                </Route>
                <Route path="/matches/:matchId">
                    <MatchPage loggedInPlayer={loggedInPlayer} />
                </Route>
                <Route exact path='/dlc-settings'>
                    <DlcSettingsPage loggedInPlayer={loggedInPlayer} />
                </Route>
                <Route path="/admin">
                    <AdminPage />
                </Route>
                <Route path="/knownbugs">
                    <KnownBugs />
                </Route>
            </Switch>

            <Footer onLogout={logout} loggedInPlayer={loggedInPlayer} />
        </div>
    );
}

export default withRouter(App);
