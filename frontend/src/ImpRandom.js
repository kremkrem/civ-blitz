import seedrandom from 'seedrandom';
import CardInfo from "./cards/CardInfo";

let clientId = localStorage.getItem('clientId');
if (!clientId) {
    clientId = ''+seedrandom().int32();
    localStorage.setItem('clientId', clientId);
}
const rnd = seedrandom(clientId);

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