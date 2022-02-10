import React, { Component } from 'react';

import Piece from "./Piece";

import './Cell.css';

class Cell extends Component {
  render() {
    return (
      <div className="Cell">
        <Piece value={this.props.value} row={this.props.row} column={this.props.column} lastMove={this.props.lastMove}/>
      </div>
    );
  }
}

export default Cell;