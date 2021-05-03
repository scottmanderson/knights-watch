export interface IPlayerStats {
  gameCount: Number;
  winCount: Number;
  lossCount: Number;
  drawCount: Number;
  ratingsHistory: Number[];
  ratingsHistoryDates: Number[];
}

export interface IState {
  playerStats: IPlayerStats;
}
