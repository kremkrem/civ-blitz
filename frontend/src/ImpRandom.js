import CardInfo from "./cards/CardInfo";

const rnd = Math.random;

const ImpRandom = {

    getRandomInt: (exclusiveLimit) => {
        return Math.floor(rnd() * (exclusiveLimit ));
    },

    cardSort: (a, b) => {
        let x = a.cardCategory
        let y = b.cardCategory
        if (!CardInfo.getAllCategories().includes(x)) {
            x = "ZZZZ".concat(x)
        }
        if (!CardInfo.getAllCategories().includes(y)) {
            y = "ZZZZ".concat(y)
        }
        return x.localeCompare(y) || a.baseCardName.localeCompare(b.baseCardName);
    }

};

export default ImpRandom;