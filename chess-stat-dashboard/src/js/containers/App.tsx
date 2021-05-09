import React from "react";
import logo from "../../logo.svg";
import "./App.css";
import PlayerRatingHistoryChart from "../components/PlayerRatingHistoryChart";
import PlayerTimeManagementHistoryChart from "../components/PlayerTimeManagementHistoryChart";

function App() {
  return (
    <div className="App">
      <PlayerRatingHistoryChart />
      <PlayerTimeManagementHistoryChart />
    </div>
  );
}

export default App;
