import React, { Component } from 'react';

import Cell from './Cell';

import './Row.css';

class Row extends Component {
  cells() {
    var pieces = this.props.pieces ? this.props.pieces : [0, 0, 0, 0, 0, 0, 0, 0];
    return pieces.map((piece, index) =>
      <Cell key={index} value={piece} row={this.props.rowIndex} column={index} lastMove={this.props.lastMove} />
    );
  }

  render() {
    return (
      <div className="Row">
        {this.cells()}
      </div>
    );
  }
}

export default Row;