import { ActionTypes, PLAYER_STATS_LOADED } from "./actions/actionTypes";
import { initialState } from "./store";
import { IState } from "./storeTypes";

const rootReducer = (state = initialState, action: ActionTypes): IState => {
  switch (action.type) {
    case PLAYER_STATS_LOADED:
      return Object.assign({}, state, {
        playerStats: action.paylaod,
      });
    default:
      return state;
  }
};

export default rootReducer;
