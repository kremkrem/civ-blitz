import React, { useEffect, useState } from "react";
import { Button, Checkbox, Container, Dropdown, Form, Header, Segment } from "semantic-ui-react";
import ImpRandom from "./ImpRandom";
import ConstructedCiv from "./ConstructedCiv.js";
import CardStore, { MAIN_CATEGORIES } from "./cards/CardStore";
import CivCardGroup from "./cards/CivCardGroup";

const SingleDraft = () => {
    const [collection, setCollection] = useState([]);
    const [selectedCards, setSelectedCards] = useState([]);
    const [useUpgrades, setUseUpgrades] = useState(false);
    const allDlcs = CardStore.getDlcs()
    const dlcOptions = allDlcs.map(dlc => { return { key: dlc, text: dlc, value: dlc }; })
    const [dlcSettings, setDlcSettings] = useState(allDlcs);
    const rerollDraft = () => {
        let cards = [];
        cards = [].concat(...(MAIN_CATEGORIES.map(cat => {
            return CardStore.getUniqueCardsFromCategory(cat, 4, useUpgrades, dlcSettings);
        })));
        cards.sort(ImpRandom.cardSort);
        setCollection(cards);
        setSelectedCards([]);
    };

    useEffect(rerollDraft, []);

    const collectionCardClicked = (card) => {
        const maxOneOfCategory = MAIN_CATEGORIES.includes(card.cardCategory);

        let updatedCollection = [].concat(collection).filter(c => c !== card);
        let newSelectedCards = [].concat(selectedCards.filter(selected => selected.cardCategory !== card.cardCategory));
        newSelectedCards.push(card);
        newSelectedCards.sort(ImpRandom.cardSort);

        if (maxOneOfCategory) {
            const toReplaceInCiv = selectedCards.filter(selected => selected.cardCategory === card.cardCategory);
            updatedCollection = updatedCollection.concat(toReplaceInCiv);
        }

        updatedCollection.sort(ImpRandom.cardSort);
        setCollection(updatedCollection);
        setSelectedCards(newSelectedCards);
    };

    const civCardClicked = (card) => {
        let newSelectedCards = selectedCards.filter(c => c !== card);
        newSelectedCards.sort(ImpRandom.cardSort);

        let updatedCollection = [].concat(collection);
        updatedCollection.push(card);
        updatedCollection.sort(ImpRandom.cardSort);

        setSelectedCards(newSelectedCards);
        setCollection(updatedCollection);
    };

    const onDlcChange = (e, { value }) => setDlcSettings(value)

    return (
        <React.Fragment>
            <Container>
                <Segment>
                    <Header as='h3'>Single draft options</Header>
                    <Form>
                        <Form.Field>
                            <Checkbox toggle label='Include upgraded (non-common) cards in the draft'
                                checked={useUpgrades} onChange={(e, data) => setUseUpgrades(data.checked)} />
                        </Form.Field>
                        <Form.Field>
                            <Dropdown
                                placeholder='DLCs'
                                fluid
                                multiple
                                search
                                selection
                                options={dlcOptions}
                                value={dlcSettings}
                                onChange={onDlcChange}
                            />
                        </Form.Field>
                        <Button onClick={rerollDraft}>Draft new cards</Button>
                    </Form>
                </Segment>
                <ConstructedCiv index='single draft' cards={selectedCards} editable={true} alwaysEditing={true}
                    onCardClick={civCardClicked} />
            </Container>
            <Container style={{ marginTop: '1em' }}>
                <Header as='h2'>Drafted cards</Header>
                <CivCardGroup cards={collection} cardClicked={collectionCardClicked} />
            </Container>
        </React.Fragment>
    );
}

export default SingleDraft;