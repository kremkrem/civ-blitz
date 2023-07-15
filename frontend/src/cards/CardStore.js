import ImpRandom from "../ImpRandom";

let allCards = [];
let dlcs = [];

export const CATEGORIES = ["CivilizationAbility", "LeaderAbility", "UniqueInfrastructure", "UniqueUnit", "Power", "ActOfGod"];
export const MAIN_CATEGORIES = ["CivilizationAbility", "LeaderAbility", "UniqueInfrastructure", "UniqueUnit"];
export const RARITY_WEIGHTS = {"Common": 15, "Uncommon": 4, "Rare": 3, "Epic": 2};

const byCategoryAndRarity = {};
const mediaByCivType = {};

const CardStore = {

    initialised: false,

    addCards: (cards) => {
        if (!CardStore.initialised) {
            allCards = allCards.concat(cards);
            cards.forEach(card => {
                if (!(card.cardCategory in byCategoryAndRarity)) {
                    byCategoryAndRarity[card.cardCategory] = {};
                }
                if (!(card.rarity in byCategoryAndRarity[card.cardCategory])) {
                    byCategoryAndRarity[card.cardCategory][card.rarity] = [];
                }
                byCategoryAndRarity[card.cardCategory][card.rarity].push(card);
                if (card.cardCategory === 'CivilizationAbility') {
                    mediaByCivType[card.civilizationType] = card.mediaName;
                }
                if (!(dlcs.includes(card.requiredDlc))) {
                    dlcs.push(card.requiredDlc);
                }
            });
            CardStore.initialised = true;
        }
    },

    getAll: () => {
        return allCards;
    },

    getDlcs: () => {
        return dlcs;
    },

    getMediaNameForCivType: (civType) => {
        return mediaByCivType[civType];
    },

    getUniqueCardsFromCategory: (category, numCards, upgraded=false, dlcList=dlcs) => {
        let selected = [];
        let rarityWeightedList = ["Common"];

        if (upgraded) {
            rarityWeightedList = [].concat(...(
                Object.keys(RARITY_WEIGHTS).map((rarity) => {
                    let count = RARITY_WEIGHTS[rarity];
                    return (rarity in byCategoryAndRarity[category]) ? Array(count).fill(rarity) : [];
                })
            ));
        }
        let failureCounter = 0;
        while (selected.length < numCards) {
            const rarity = rarityWeightedList[ImpRandom.getRandomInt(rarityWeightedList.length)];
            const cardList = byCategoryAndRarity[category][rarity].filter(
                card =>
                    !selected.some((c) => c.identifier == card.identifier) &&
                    dlcList.some((dlc) => dlc == card.requiredDlc));
            if (cardList.length > 0) {
                const possibleCard = cardList[ImpRandom.getRandomInt(cardList.length)];
                selected.push(possibleCard);
            } else {
                failureCounter += 1;
                if (failureCounter > 10) {
                    break;
                }
            }
        }
        return selected;
    }
};

export default CardStore;