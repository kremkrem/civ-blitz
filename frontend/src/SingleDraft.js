import React, {useEffect, useState} from "react";
import {Container, Header} from "semantic-ui-react";
import ImpRandom from "./ImpRandom";
import ConstructedCiv from "./ConstructedCiv.js";
import CardStore, {MAIN_CATEGORIES} from "./cards/CardStore";
import CivCardGroup from "./cards/CivCardGroup";

const SingleDraft = () => {
    const [collection, setCollection] = useState([]);
    const [selectedCards, setSelectedCards] = useState([]);

    useEffect(() => {
        let cards = [];
        cards = [].concat(...(MAIN_CATEGORIES.map(cat => {
            return CardStore.getUniqueCardsFromCategory(cat, 4);
        })));
        cards.sort(ImpRandom.cardSort);
        setCollection(cards);
    }, []);

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

    return (
        <React.Fragment>
            <Container>
                <ConstructedCiv index='single draft' cards={selectedCards} editable={true} alwaysEditing={true}
                                onCardClick={civCardClicked} />
            </Container>
            <Container style={{marginTop: '1em'}}>
                <Header as='h2'>Drafted cards</Header>
                <CivCardGroup cards={collection} cardClicked={collectionCardClicked} />
            </Container>
        </React.Fragment>
    );
}

export default SingleDraft;