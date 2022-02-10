import React, { Component } from 'react';

import Sidebar from './Sidebar';

import './PlayerTwoSidebar.css';

class PlayerTwoSidebar extends Component {
  render() {
    return (
      <div className="PlayerTwoSidebar">
        <Sidebar name={this.props.name} active={this.props.currentPlayer === "player-two"} connected={this.props.connected} />
      </div>
    );
  }
}

export default PlayerTwoSidebar;


