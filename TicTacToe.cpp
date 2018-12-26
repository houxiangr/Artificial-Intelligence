#include<stdio.h>
#define COM -1 
#define MAN 1
#define STEP 9 
#define DRAW 0 
#define ROW 3 
#define COL 3
#define MAX_NUM 1000;
struct Move{
	int x;
	int y;
 
};
//棋盘
int board[3][3] = {
	{ 0,0,0 },
	{ 0,0,0 },
	{ 0,0,0 }
};
int tempBoard[3][3] = {
	{ 0,0,0 },
	{ 0,0,0 },
	{ 0,0,0 }
};
int player = MAN;
Move bestMove;
int currentDepth;
bool MAN_first = true;
int isWin() {
	int i, j;
	for (int i = 0; i < 3; i++)
	{
		if (board[i][0] + board[i][1] + board[i][2] == 3)
			return 1;
		else if (board[i][0] + board[i][1] + board[i][2] == -3)
			return -1;
	}
	for (int j = 0; j < 3; j++)
	{
		if (board[0][j] + board[1][j] + board[2][j] == 3)
			return 1;
		else if (board[0][j] + board[1][j] + board[2][j] == -3)
			return -1;
	}
	if (board[0][0] + board[1][1] + board[2][2] == 3 || board[0][2] + board[1][1] + board[2][0] == 3)
		return 1;
	else if (board[0][0] + board[1][1] + board[2][2] == -3 || board[0][2] + board[1][1] + board[2][0] == -3)
		return -1;
	else  return 0;
}
int evaluteMap() {
	bool flag = true;
	int i, j;
 
	if (isWin() == COM)
		return MAX_NUM;
	if (isWin() == MAN)
		return -MAX_NUM;
	int count = 0;
	for (i = 0; i < 3; i++)
		for (j = 0; j < 3; j++){
			if (board[i][j] == 0)
				tempBoard[i][j] = COM;
			else
				tempBoard[i][j] = board[i][j];
		}
	for (i = 0; i < 3; i++)
		count += (tempBoard[i][0] + tempBoard[i][1] + tempBoard[i][2]) / 3;
	for (i = 0; i < 3; i++)
		count += (tempBoard[0][i] + tempBoard[1][i] + tempBoard[2][i]) / 3;
	count += (tempBoard[0][0] + tempBoard[1][1] + tempBoard[2][2]) / 3;
	count += (tempBoard[2][0] + tempBoard[1][1] + tempBoard[0][2]) / 3;
	for (i = 0; i < 3; i++)
		for (j = 0; j < 3; j++)
		{
			if (board[i][j] == 0)
				tempBoard[i][j] = MAN;
			else tempBoard[i][j] = board[i][j];
		}
	for (i = 0; i < 3; i++)
		count += (tempBoard[i][0] + tempBoard[i][1] + tempBoard[i][2]) / 3;
	for (i = 0; i < 3; i++)
		count += (tempBoard[0][i] + tempBoard[1][i] + tempBoard[2][i]) / 3;
	count += (tempBoard[0][0] + tempBoard[1][1] + tempBoard[2][2]) / 3;
	count += (tempBoard[2][0] + tempBoard[1][1] + tempBoard[0][2]) / 3;
	return count;
}
void makeMove(Move curMove){
	board[curMove.x][curMove.y] = player;
	player = (player == COM) ? MAN : COM;
}

void unMakeMove(Move curMove) {
	board[curMove.x][curMove.y] = 0;
	player = (player == COM) ? MAN : COM;
}
int getMoveList(Move moveList[]) {
	int moveCount = 0;
	int i, j;
	for (i = 0; i < COL; i++)
	{
		for (j = 0; j < ROW; j++)
		{
			if (board[i][j] == 0)
			{
				moveList[moveCount].x = i;
				moveList[moveCount].y = j;
				moveCount++;
			}
 
		}
	}
	return moveCount;
}

int miniMaxsearch(int depth){
	int value;
	int bestValue = 0;
	int moveCount = 0;
	int i;
	int m, n;
	Move moveList[9];
	if (isWin() == COM || isWin() == MAN){
		return evaluteMap();
	}
	if (depth == 0){
		return evaluteMap();
	}
	if (COM == player) {
		bestValue = -MAX_NUM;
	}else if (MAN == player){
		bestValue = MAX_NUM;
	}
	moveCount = getMoveList(moveList);
 
	for (i = 0; i < moveCount; i++){
		Move curMove = moveList[i]; 
		makeMove(curMove);
		value = miniMaxsearch(depth - 1);
		unMakeMove(curMove);
 
		if (player == COM){
			if (value > bestValue){
				bestValue = value;
				if (depth == currentDepth){
					bestMove = curMove;
				}
			}
		}else if (player == MAN){
			if (value < bestValue)
			{
				bestValue = value;
				if (depth == currentDepth)
				{
					bestMove = curMove;
				}
			}
		}
 
	}
	return bestValue;
}
void printchess() {
	int i, j;
	for (i = 0; i < COL; i++)
	{
		for (j = 0; j < ROW; j++)
		{
			if (board[i][j] == COM)
			{
				printf("| X ");
			}
			else if (board[i][j] == MAN)
			{
				printf("| O ");
			}
			else
			{
				printf("|   ");
			}
 
		}
		printf("| \n");
	}
	printf("\n\n");

} 
void computer_play() {
	miniMaxsearch(currentDepth);
	board[bestMove.x][bestMove.y] = COM;
 
}
void player_play() {
	int x, y;
	printf("请输入位置坐标  e.g ：（0 0）为左上角 （2，2）为右下角 \n");
	scanf("%d", &x);
	scanf("%d", &y);
 
	while (x < 0 || x > 2 || y < 0 || y > 2)
	{
		printf("您输入的坐标错误，请重新输入:x:(0~2) , y:(0~2)\n");
		scanf("%d", &x);
		scanf("%d", &y);
	}
	while (board[x][y] != 0)
	{
		printf("该位置已有棋，请重新输入:\n");
		scanf("%d", &x);
		scanf("%d", &y);
	}
 
	board[x][y] = MAN;
 
}
void setFirst() {
	char c;
	printf("\nDo you want to play first? y -你先走  , n-电脑先走");
	for (c = getchar(); c != 'Y'&&c != 'y'&&c != 'N'&&c != 'n'; c = getchar());
	if (c == 'n' || c == 'N') {
		MAN_first = false;
	}
	printf("\n");
}
int main(){
	currentDepth = 9;
	int step = 1;
	setFirst();
	printchess();
	if (MAN_first) {
		player = MAN;
		for (step = 1; step <= STEP; )
		{
			player_play();
			printchess();
			if (player == isWin()) {
				printf("您获胜了！！");
				break;
			}
			step++;
			currentDepth--;
			if (step == 10) {
				printf("平局 ~~~");
				break;
			}
			player = (player == COM) ? MAN : COM;
			computer_play();
			printchess();
			if (player == isWin()) {
				printf("很遗憾，电脑赢啦！！！");
				break;
			}
			step++;
			currentDepth--;
			player = (player == COM) ? MAN : COM;
		}
	}else{
		player = COM;
		for (step = 1; step <= STEP; )
		{
			computer_play();
			printchess();
			if (player == isWin()) {
				printf("很遗憾，电脑赢啦！！！");
				break;
			}
			step++;
			currentDepth--;
			if (step == 10) {
				printf("平局 ~~~");
				break;
			}
			player = (player == COM) ? MAN : COM;
			player_play();
			printchess();
			if (player == isWin()) {
				printf("您获胜了！！");
				break;
			}
			step++;
			currentDepth--;
			player = (player == COM) ? MAN : COM;
		}
	}
	getchar();
}
