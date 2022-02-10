import React, { Component } from 'react';
import Row from './Row'
import './Board.css';

class Board extends Component {
  rows() {
    var rows = this.props.data ? this.props.data : [];
    return rows.map((row, index) =>
      <Row key={index} pieces={row} rowIndex={index} lastMove={this.props.lastMove} />
    );
  }

  render() {
    return (
      <div className="Board">
        {this.rows()}
      </div>
    );
  }
}

export default Board;


