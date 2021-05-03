import React, { useEffect } from "react";
import { useTheme } from "@material-ui/core";
import Plot from "react-plotly.js";
import { useDispatch, useSelector } from "react-redux";
import { IState } from "../redux/storeTypes";
import { getPlayerStats } from "../redux/actions/actionCreators";

const PlayerRatingHistoryChart: React.FC = () => {
  const theme = useTheme();
  const playerStats = useSelector((state: IState) => state.playerStats);
  const dispatch = useDispatch();

  useEffect(() => {
    dispatch(getPlayerStats("2020-12-31"));
  }, [dispatch]);

  return (
    <Plot
      data={[
        {
          name: "Rating",
          x: playerStats.ratingsHistoryDates.map((x) => new Date(x)),
          y: playerStats.ratingsHistory,
        },
      ]}
      layout={{
        title: {
          text: "Lichess Rating History",
        },
      }}
    />
  );
};

export default PlayerRatingHistoryChart;
