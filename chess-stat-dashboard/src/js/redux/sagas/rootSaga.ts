import { all } from "redux-saga/effects";
import getPlayerStatsSaga from "./getPlayerStatsSaga";

export default function* rootSaga() {
  yield all([getPlayerStatsSaga()]);
}
