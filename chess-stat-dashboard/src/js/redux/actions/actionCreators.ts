import { IPlayerStatsRequested, PLAYER_STATS_REQUESTED } from "./actionTypes";

export function getPlayerStats(after?: string): IPlayerStatsRequested {
  return {
    type: PLAYER_STATS_REQUESTED,
    after: after,
  };
}
