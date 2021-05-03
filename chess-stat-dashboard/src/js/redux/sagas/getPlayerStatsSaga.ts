import { takeEvery, call, put, putResolve } from "redux-saga/effects";
import {
  API_ERRORED,
  IApiErrored,
  IPlayerStatsLoaded,
  IPlayerStatsRequested,
  PLAYER_STATS_LOADED,
  PLAYER_STATS_REQUESTED,
} from "../actions/actionTypes";

import { apiRoot } from "../../helpers";

function getPlayerStats(after: string) {
  let queryString = after ? "?" : "";
  if (after) queryString += "after=" + after;
  return fetch(apiRoot + "/statistics" + queryString).then((response) =>
    response.json()
  );
}

function* workerSaga(action: IPlayerStatsRequested): any {
  let after = action.after;
  try {
    // @ts-ignore
    const payload = yield call(getPlayerStats, after);
    yield putResolve({ type: PLAYER_STATS_LOADED });
  } catch (e) {
    yield put({ type: API_ERRORED, payload: e });
  }
}

export default function* watcherSaga() {
  yield takeEvery(PLAYER_STATS_REQUESTED, workerSaga);
}
