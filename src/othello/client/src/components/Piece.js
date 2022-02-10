import React, { Component } from 'react';

import './Piece.css';

var classNames = require('classnames');

class Piece extends Component {

  isCurrentMove() {
    return this.props.lastMove
      && this.props.lastMove[0] === this.props.row
      && this.props.lastMove[1] === this.props.column;
  }

  render() {
    var pieceClasses = classNames("Piece", "Piece-" + this.props.value, {
      "Piece-latest": this.isCurrentMove()
    });
    return (
      <div className={pieceClasses}/>
    );
  }
}

export default Piece;