import { createStore, applyMiddleware, compose } from "redux";
import createSagaMiddleware from "redux-saga";
import logger from "redux-logger";

import rootReducer from "./rootReducer";
import rootSaga from "./sagas/rootSaga";
import { IState } from "./storeTypes";

const reduxDevTools =
  // @ts-ignore
  window.__REDUX_DEVTOOLS_EXTENSION__ && window.__REDUX_DEVTOOLS_EXTENSION__();

const sagaMiddleware = createSagaMiddleware();
// @ts-ignore
const storeEnhancers = window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ || compose;

export const initialState: IState = {
  playerStats: {
    gameCount: 1,
    winCount: 0,
    lossCount: 0,
    drawCount: 1,
    ratingsHistory: [1500],
    ratingsHistoryDates: [1619987320226],
    playerTimeEndingSurpluses: [0],
    playerRatingDiff: [0],
    score: [0.5],
  },
};

const store = createStore(
  rootReducer,
  initialState,
  storeEnhancers(applyMiddleware(sagaMiddleware, logger))
);

sagaMiddleware.run(rootSaga);

export default store;
