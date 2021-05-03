import { IPlayerStats } from "../storeTypes";

export const PLAYER_STATS_REQUESTED = "PLAYER_STATS_REQUESTED";

export interface IPlayerStatsRequested {
  type: typeof PLAYER_STATS_REQUESTED;
  after?: string;
}

export const PLAYER_STATS_LOADED = "PLAYER_STATS_LOADED";

export interface IPlayerStatsLoaded {
  type: typeof PLAYER_STATS_LOADED;
  payload: IPlayerStats;
}

export const API_ERRORED = "API_ERRORED";

export interface IApiErrored {
  type: typeof API_ERRORED;
  payload: any;
}

export type ActionTypes =
  | IPlayerStatsRequested
  | IPlayerStatsLoaded
  | IApiErrored;
