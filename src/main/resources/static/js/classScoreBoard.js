const frameNum = 10;
const firstFrameNum = 1;
const lastFrameNum = 10;
const gutter = 0;
const allPinsDown = 10;
const firstTry = 1;
const secondTry = 2;
const finalTryAt10Frame = 3;
const transEnum = {
    FRAME: "FRAME", // NEXT FRAME
    PHASE: "PHASE", // NEXT PHASE
    PHS10: "PHS10", // NEXT PHASE AT FRAME 10
    ENDGM: "ENDGM", // END GAME
};
const scoreEnum = {
    GUTTER: "GUTTER", // GUTTER '-'
    STRIKE: "STRIKE", // STRIKE 'X'
    SPARE: "SPARE",   // SPARE  '/'
};

class ScoreBoard {
    constructor() {
        this.frame = Array(frameNum).fill(null).map(() => Array(secondTry + 1).fill());
        this.frame[lastFrameNum] = new Array(finalTryAt10Frame + 1);
        this.frameScore = Array(frameNum).fill();
        this.curFrameNum = firstFrameNum;
        this.curPhaseNum = firstTry;
        this.lastCalcFrameNum = firstFrameNum;
        this.totalScore = 0;
        this.calcFrameScoreBuffer = new Array(frameNum + 1).fill();
        this.scoreData = new Array();
    }

    getCurFrameNum() {
        return this.curFrameNum;
    }

    getCurPhaseNum() {
        return this.curPhaseNum;
    }

    getLastCalcFrameNum() {
        return this.lastCalcFrameNum;
    }

    getTotalScore() {
        return this.totalScore;
    }

    getScoreData() {
        return this.scoreData;
    }

    getTryScore(frameNum, phaseNum) {
        return this.frame[frameNum][phaseNum];
    }
    
    setTryScore(frameNum, phaseNum, score) {
        this.frame[frameNum][phaseNum] = score;
        // frame에 score 저장 후, 서버로 전송할 scoreData 배열 object에도 score 추가
        this.scoreData.push(score);
    }
    
    getFrameScore(frameNum) {
        return this.frameScore[frameNum];
    }

    setFrameScore(frameNum, score) {
        this.frameScore[frameNum] = score;
    }

    // 현재 시점 이전 시도에 얻은 점수값 획득
    getBeforeTryScore(frameNum, phaseNum) {
        // 각 프레임별 최대 시도횟수
        let framePhaseCount = this.frame[frameNum].length;

        // 현재 프레임에서 1차 시기인 경우
        if (phaseNum === firstTry && frameNum > firstFrameNum) {
            return this.getTryScore(frameNum - 1, framePhaseCount - 1);
        }

        // 현재 프레임에서 1차 시기 이후의 시기인 경우
        if (phaseNum > firstFrameNum) {
            return this.getTryScore(frameNum, phaseNum - 1);
        }

        // 게임을 시작한 직후(1프레임 1차 시기)인 경우 null 출력
        return null;
    }

    getNextTryScore(frameNum, phaseNum) {
        // 각 프레임별 최대 시도횟수
        let curScore = this.getTryScore(frameNum, phaseNum)
        let framePhaseCount = this.frame[frameNum].length;

        // 10프레임의 경우
        if (frameNum === lastFrameNum && phaseNum < framePhaseCount) {
            return this.getTryScore(frameNum, phaseNum + 1);
        }

        // 1차 시기
        if (phaseNum === firstTry) {
            // 현재 프레임에서 스트라이크를 한 경우
            if (curScore === allPinsDown) {
                // 다음 프레임의 1차 시기 값 반환
                return this.getTryScore(frameNum + 1, firstTry);
            }
            // 현재 프레임에서 스트라이크를 하지 못한 경우
            else {
                // 같은 프레임 2차 시기 값 반환
                return this.getTryScore(frameNum, secondTry);
            }
        }
        
        // 2차 시기
        if (phaseNum === secondTry) {
            // 다음 프레임의 1차 시기 값 반환
            return this.getTryScore(frameNum + 1, firstTry);
        }

        // 마지막 시도인 경우 null 출력
        return null;
    }

    decideScoreState(frameNum, phaseNum, numberResult) {
        let curScore = this.getTryScore(frameNum, phaseNum);
        let beforeScore = this.getBeforeTryScore(frameNum, phaseNum);
        
        // Gutter
        if (curScore === gutter) {
            return scoreEnum.GUTTER;
        }

        // 10프레임 3차 시기인 경우
        if (frameNum === lastFrameNum && phaseNum === finalTryAt10Frame) {
            // 10프레임 1차 시기 점수 획득
            let frame10Phase1Score = this.getTryScore(lastFrameNum, firstTry);

            // 1차 시기에 스트라이크를 한 경우
            if (frame10Phase1Score === allPinsDown) {
                // 2, 3차 시기에 스트라이크를 한 경우
                if (beforeScore === allPinsDown && curScore === allPinsDown) {
                    return scoreEnum.STRIKE;
                }
                // 2, 3차 시기에 스페어를 한 경우
                if (curScore + beforeScore === allPinsDown || (curScore === allPinsDown && beforeScore === gutter)) {
                    return scoreEnum.SPARE;
                }
            }

            // 1, 2차 시기에 스페어를 한 경우
            if (beforeScore + frame10Phase1Score === allPinsDown || (beforeScore === allPinsDown && frame10Phase1Score === gutter)) {
                // 3차 시기에 스트라이크를 한 경우
                if (curScore === allPinsDown) {
                    return scoreEnum.STRIKE;
                }
            }
            return numberResult;
        }
        else {
            // Strike : 10점을 냈을 때 첫 시도 또는 같은 프레임 내 이전 시기 점수가 gutter가 아닌 경우
            if (curScore === allPinsDown) {
                if (phaseNum === firstTry || (phaseNum !== firstTry && beforeScore !== gutter)) {
                    return scoreEnum.STRIKE;
                }
            }
            // Spare : 같은 프레임 내 이전 시기와 현재 시기 점수의 합이 10인 경우
            if (phaseNum !== firstTry) {
                if (curScore + beforeScore === allPinsDown || (curScore === allPinsDown && beforeScore === gutter)) {
                    return scoreEnum.SPARE;
                }
            }
        }
        // Score 1 ~ 9
        return numberResult;
    }

    // 게임의 프레임, 시기를 조정
    transFrameOrPhase(transEnumValue) {
        switch (transEnumValue) {
            case transEnum.FRAME:
                this.curFrameNum++;
                this.curPhaseNum = firstTry;
                break;
            case transEnum.PHASE:
            case transEnum.PHS10:
                this.curPhaseNum++;
                break;
            default:
                break;
        }
    }

    // 게임의 프레임, 시기 전환 결정
    // transEnumValue를 이용해 transFrameOrPhase()로 실질적 변환
    decideWhichFrameOrPhase(frameNum, phaseNum) {
        let curScore = this.getTryScore(frameNum, phaseNum);
        let beforeScore = this.getBeforeTryScore(frameNum, phaseNum);
        let transEnumValue;
        
        // 프레임 당 시도횟수로 구별
        // 1. 해당 시기에 10점을 얻었을 때
        if (curScore === allPinsDown) {
            // 10프레임 3차 시기인 경우 게임 종료
            if (frameNum === lastFrameNum && phaseNum === finalTryAt10Frame) {
                transEnumValue = transEnum.ENDGM;
            }
            // 10프레임 1, 2차 시기인 경우 다음 시기로 넘어감
            else if (frameNum === lastFrameNum) {
                transEnumValue = transEnum.PHS10;
            }
            // 10프레임 제외 모든 핀을 쓰러트리면 다음 프레임으로 전환
            else {
                transEnumValue = transEnum.FRAME;
            }
        }
        // 2. 10점 미만의 점수를 얻었을 때
        else {
            // 1차 시기
            if (phaseNum === firstTry) {
                transEnumValue = transEnum.PHASE;
            }
            // 현재 10프레임 2차 시기인 상황에서, 1차 시기에 스트라이크를 한 경우
            else if (frameNum == lastFrameNum && phaseNum == secondTry && beforeScore === allPinsDown) {
                transEnumValue = transEnum.PHASE;
            }
            // 2(, 3)차 시기에 스페어 처리한 경우
            else if (beforeScore + curScore === allPinsDown) {
                // 10프레임 2차 시기에 스페어를 한 경우 3차 시기로 전환
                if (frameNum === lastFrameNum && phaseNum == secondTry) {
                    transEnumValue = transEnum.PHS10;
                }
                // 10프레임 3차 시기에 스페어를 한 경우 게임 종료
                else if (frameNum === lastFrameNum && phaseNum == finalTryAt10Frame) {
                    transEnumValue = transEnum.ENDGM;
                }
                // 보통의 경우 스페어를 친 경우 다음 프레임으로 전환
                else {
                    transEnumValue = transEnum.FRAME;
                }
            } else {
                // 10프레임에 스페어를 못 한 경우 게임 종료
                if (frameNum == lastFrameNum) {
                    transEnumValue = transEnum.ENDGM;
                }
                // 보통의 경우 스페어를 친 경우 다음 프레임으로 전환
                else {
                    transEnumValue = transEnum.FRAME;
                }
            }
        }
        // 프레임/시기 결정 기준값(transEnumValue) 반환
        return transEnumValue;
    }

    setFrameScoreBuffer(frameNum, transEnumValue, scoreStateValue) {
        // STRIKE : 2번의 후속 클릭 후 점수 계산
        if (scoreStateValue === scoreEnum.STRIKE) {
            this.calcFrameScoreBuffer[frameNum] = 2;
        }
        // SPARE : 1번의 후속 클릭 후 점수 계산
        else if (scoreStateValue === scoreEnum.SPARE) {
            this.calcFrameScoreBuffer[frameNum] = 1;
        }
        // 그 외 : 프레임 시기가 끝난 직후 점수 계산
        else {
            this.calcFrameScoreBuffer[frameNum] = 0;
        }

        // 게임을 종료하는 시점이면 모든 버퍼 내용을 0으로 세팅
        // 게임 종료 시점에 자동으로 모든 스코어를 계산하도록 유도
        if (transEnumValue === transEnum.ENDGM) {
            for (let i = firstFrameNum; i <= lastFrameNum; i++) {
                this.calcFrameScoreBuffer[i] = 0;
            }
        }
    }

    decideCalcFrameScore(frameNum, transEnumValue, scoreStateValue) {
        if (transEnumValue === transEnum.FRAME || transEnumValue === transEnum.ENDGM) {
            this.setFrameScoreBuffer(frameNum, transEnumValue, scoreStateValue);
        }

        if (transEnumValue !== transEnum.ENDGM) {
            for (let i = this.lastCalcFrameNum; i < frameNum; i++) {
                this.calcFrameScoreBuffer[i]--;
            }
        }

        while (this.calcFrameScoreBuffer[this.lastCalcFrameNum] === 0) {
            this.calcFrameScore(this.lastCalcFrameNum, scoreStateValue);
            this.lastCalcFrameNum++;
        }
    }

    // 버튼 클릭 후 스코어판에 점수를 기록
    insertTryScoreIntoFrameView(frameNum, phaseNum, scoreState) {
        let scoreValue = this.decideScoreState(frameNum, phaseNum, scoreState);
        let scoreChar;
        switch (scoreValue) {
            case scoreEnum.GUTTER:
                scoreChar = '-';
                break;
            case scoreEnum.STRIKE:
                scoreChar = 'X';
                break;
            case scoreEnum.SPARE:
                scoreChar = '/';
                break;
            default:
                scoreChar = scoreValue;
                break;
        }

        // 스트라이크는 스코어판의 2차 시기 영역에 표시(10프레임 제외)
        if (scoreValue === scoreEnum.STRIKE && frameNum !== lastFrameNum) {
            $(`#frame${frameNum}phase${secondTry}`).text(scoreChar);
        }
        else {
            $(`#frame${frameNum}phase${phaseNum}`).text(scoreChar);
        }
    }

    insertFrameScoreIntoFrameView(frameNum, score, totalScore) {
        $(`#frame${frameNum}score`).text(score);
        $("#totalScore").text(totalScore);
    }

    // 프레임별 점수 계산 메소드
    calcFrameScore(frameNum) {
        const phase1 = this.getTryScore(frameNum, firstTry);
        const phase2 = this.getTryScore(frameNum, secondTry) || 0;
        let nextFramePhase1 = 0;
        let nextFramePhase2 = 0;

        // 1, 2차 시기 점수 합함
        let score = phase1 + phase2;
        
        // 10프레임은 보너스 점수 없음, 3차 시기 점수를 합함
        if (frameNum === lastFrameNum && score >= allPinsDown) {
            const phase3 = this.getTryScore(frameNum, finalTryAt10Frame);
            score += phase3;
        } else {
            // 스페어, 한 번의 기회 추가 점수 획득
            if (score === allPinsDown) {
                nextFramePhase1 = this.getNextTryScore(frameNum, secondTry);
            }

            // 스트라이크, 두 번의 기회 추가 점수 획득
            // 두 프레임 연속 스트라이크를 한 경우 2번째 보너스 점수는 현재 프레임의 다다음 프레임의 1차 시기 점수
            if (phase1 === allPinsDown && nextFramePhase1 === allPinsDown && frameNum < lastFrameNum - 1) {
                nextFramePhase2 = this.getTryScore(frameNum + 2, firstTry);
            }
            // 9프레임에서 스트라이크를 한 경우 2번째 보너스 점수는 10프레임 2차 시기 점수
            else if (phase1 === allPinsDown && nextFramePhase1 === allPinsDown && frameNum === lastFrameNum - 1) {
                nextFramePhase2 = this.getTryScore(frameNum + 1, secondTry);
            }
            // 1번째 보너스 점수가 스트라이크가 아닌 경우 2번째 보너스 점수는 그 프레임의 2차 시기 점수
            else if (phase1 === allPinsDown && nextFramePhase1 !== allPinsDown && frameNum < lastFrameNum) {
                nextFramePhase2 = this.getTryScore(frameNum + 1, secondTry);
            }
            score += (nextFramePhase1 + nextFramePhase2);
        }
        // 지난 프레임 점수 누적
        if (frameNum > firstFrameNum) {
            score += this.getFrameScore(frameNum - 1);
        }
        
        this.frameScore[frameNum] = score;
        this.totalScore = score;
        this.insertFrameScoreIntoFrameView(frameNum, score, this.totalScore);
    }
}
