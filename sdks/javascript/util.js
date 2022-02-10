// The game will not ask a player for a move if the player has no valid move available.
function getMove(player, board) {
	if (player != 1 && player != 2) throw new Error('invalid player')
	if (board.length > 8 || board[0].length > 8) throw new Error('invalid board')

	let p2 = 0
	if (player === 1) p2 = 2
	if (player === 2) p2 = 1
	let out = []
	let maxGain = 0
	let discsToFlip = []
	let newBoard = []
	copyBoard(newBoard, board)

	// walk thru board and calculate the number of points gained if disc is placed at location
	for (let row = 0; row < 8; row++) {
		for (let col = 0; col < 8; col++) {
			if (board[row][col] === 0) {
				out = calculateGains(player, board, row, col)
				// in case multiple moves have same amt of gains - just choose the first one
				if (out[0] > maxGain) {
					maxGain = out[0]
					discsToFlip = copyArray(out[1])
					discsToFlip.push([row, col])
				}
			}
		}
	}

	console.log('Old Board: ')
	console.log(board)
	// update board state
	for (let coord of discsToFlip) {
		let r = coord[0]
		let c = coord[1]
		newBoard[r][c] = player
	}
	console.log('New Board: ')
	console.log(newBoard)

	return discsToFlip[discsToFlip.length - 1]
}

function copyBoard(newBoard, old) {
	let row = []
	for (let r = 0; r < 8; r++) {
		for (let c = 0; c < 8; c++) {
			row.push(old[r][c])
		}
		pushRow(newBoard, row)
		row = []
	}
}

function pushRow(board, row) {
	let arr = []
	for (let r of row) {
		arr.push(r)
	}
	board.push(arr)
}

function copyArray(old) {
	let final = []
	for (let coord of old) {
		final.push(coord)
	}
	return final
}

function calculateGains(player, board, row, col) {
	let p2 = -1
	if (player === 1) p2 = 2
	if (player === 2) p2 = 1
	let potentialGain = 0
	let gain = 0
	let tmp = []
	let discsToFlip = []
	let r = 0
	let c = 0
	// if i can walk top left
	if (row - 1 >= 0 && col - 1 >= 0) {
		r = row - 1
		c = col - 1
		// continue to do so until I'm either out of bounds or the disc at that coordinate does not belong to p2
		while (r >= 0 && c >= 0 && board[r][c] === p2) {
			tmp.push([r, c])
			potentialGain++
			r = r - 1
			c = c - 1
		}
		// if having walked this way I run into my own disc - this coordinate [row, col] is a valid move
		if (r >= 0 && c >= 0 && board[r][c] === player) {
			addToArray(discsToFlip, tmp)
			gain += potentialGain
			tmp = []
			potentialGain = 0
		} else {
			tmp = []
			potentialGain = 0
		}
	}
	// rinse and repeat for each direction
	// top
	if (row - 1 >= 0) {
		r = row - 1
		c = col
		while (r >= 0 && board[r][c] === p2) {
			tmp.push([r, c])
			potentialGain++
			r = r - 1
		}
		if (r >= 0 && board[r][c] === player) {
			addToArray(discsToFlip, tmp)
			gain += potentialGain
			tmp = []
			potentialGain = 0
		} else {
			tmp = []
			potentialGain = 0
		}
	}
	// top right
	if (row - 1 >= 0 && col + 1 <= 7) {
		r = row - 1
		c = col + 1
		while (r >= 0 && c <= 7 && board[r][c] === p2) {
			tmp.push([r, c])
			potentialGain++
			r = r - 1
			c = c + 1
		}
		if (r >= 0 && c <= 7 && board[r][c] === player) {
			addToArray(discsToFlip, tmp)
			gain += potentialGain
			tmp = []
			potentialGain = 0
		} else {
			tmp = []
			potentialGain = 0
		}
	}
	// right
	if (col + 1 <= 7) {
		c = col + 1
		r = row
		while (c <= 7 && board[r][c] === p2) {
			tmp.push([r, c])
			potentialGain++
			c = c + 1
		}
		if (c <= 7 && board[r][c] === player) {
			addToArray(discsToFlip, tmp)
			gain += potentialGain
			tmp = []
			potentialGain = 0
		} else {
			tmp = []
			potentialGain = 0
		}
	}
	// bottom right
	if (row + 1 <= 7 && col + 1 <= 7) {
		r = row + 1
		c = col + 1
		while (r <= 7 && c <= 7 && board[r][c] === p2) {
			tmp.push([r, c])
			potentialGain++
			r = r + 1
			c = c + 1
		}
		if (r <= 7 && c <= 7 && board[r][c] === player) {
			addToArray(discsToFlip, tmp)
			gain += potentialGain
			tmp = []
			potentialGain = 0
		} else {
			tmp = []
			potentialGain = 0
		}
	}
	// bottom
	if (row + 1 >= 0) {
		r = row + 1
		c = col
		while (r <= 7 && board[r][c] === p2) {
			tmp.push([r, c])
			potentialGain++
			r = r + 1
		}
		if (r <= 7 && board[r][c] === player) {
			addToArray(discsToFlip, tmp)
			gain += potentialGain
			tmp = []
			potentialGain = 0
		} else {
			tmp = []
			potentialGain = 0
		}
	}
	// bottom left
	if (row + 1 <= 7 && col - 1 >= 0) {
		r = row + 1
		c = col - 1
		while (r <= 7 && c >= 0 && board[r][c] === p2) {
			tmp.push([r, c])
			potentialGain++
			r = r + 1
			c = c - 1
		}
		if (r <= 7 && c >= 0 && board[r][c] === player) {
			addToArray(discsToFlip, tmp)
			gain += potentialGain
			tmp = []
			potentialGain = 0
		} else {
			tmp = []
			potentialGain = 0
		}
	}
	// left
	if (col - 1 >= 0) {
		c = col - 1
		r = row
		while (c >= 0 && board[r][c] === p2) {
			tmp.push([r, c])
			potentialGain++
			c = c - 1
		}
		if (c >= 0 && board[r][c] === player) {
			addToArray(discsToFlip, tmp)
			gain += potentialGain
			tmp = []
			potentialGain = 0
		} else {
			tmp = []
			potentialGain = 0
		}
	}
	// The game will not ask a player for a move if the player has no valid move available.
	return [gain, discsToFlip]
}

function addToArray(final, tmp) {
	for (let t of tmp) {
		final.push(t)
	}
}

function prepareResponse(move) {
	const response = `${JSON.stringify(move)}\n`
	console.log(`Sending response ${response}`)
	return response
}

module.exports = { getMove, prepareResponse }
