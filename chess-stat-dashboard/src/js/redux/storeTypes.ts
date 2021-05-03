export interface IPlayerStats {
  gameCount: number;
  winCount: number;
  lossCount: number;
  drawCount: number;
  ratingsHistory: number[];
  ratingsHistoryDates: number[];
}

export interface IState {
  playerStats: IPlayerStats;
}
