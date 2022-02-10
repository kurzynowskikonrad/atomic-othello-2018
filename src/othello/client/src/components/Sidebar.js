import React, { Component } from 'react';

import Spinner from "./Spinner";

import './Sidebar.css';

var classNames = require('classnames');

class Sidebar extends Component {
  render() {
    var playerNameClass = classNames({'connected': this.props.connected});
    return (
      <div className="Sidebar">
        <div className={playerNameClass}>{this.props.name}</div>
        <Spinner active={this.props.active} />
      </div>
    );
  }
}

export default Sidebar;