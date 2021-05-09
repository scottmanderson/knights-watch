export interface IPlayerStats {
  gameCount: number;
  winCount: number;
  lossCount: number;
  drawCount: number;
  ratingsHistory: number[];
  ratingsHistoryDates: number[];
  playerTimeEndingSurpluses: number[];
  playerRatingDiff: number[];
  score: number[];
}

export interface IState {
  playerStats: IPlayerStats;
}
