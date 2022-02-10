import React, { Component } from 'react';

import Sidebar from './Sidebar';

import './PlayerOneSidebar.css';

class PlayerOneSidebar extends Component {
  render() {
    return (
      <div className="PlayerOneSidebar">
        <Sidebar name={this.props.name} active={this.props.currentPlayer === "player-one"} connected={this.props.connected} />
      </div>
    );
  }
}

export default PlayerOneSidebar;


