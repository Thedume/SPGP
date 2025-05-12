# Endless Runner

## 프로젝트 제목
**Endless Runner**

## 게임 컨셉
- **High Concept**:  
  플레이어는 끊임없이 앞으로 달리는 캐릭터를 조작하여 장애물을 피하고 가능한 한 오래 살아남는 것을 목표로 한다.

- **핵심 메카닉**:
  - 자동 전진 캐릭터
  - 위/아래/양 옆 스와이프 제스처를 통해 점프 및 슬라이드 동작 수행
  - 장애물과 충돌 시 목숨 1개 감소, 총 3개의 목숨 보유
  - 장애물은 랜덤하게 배치되되, 연속적으로 나오지 않도록 조정
  - 점수는 시간 기반으로 상승
  - 캐릭터는 남자/여자 중 선택
  - 게임 종료 시 플레이 시간, 이동 거리, 캐릭터 종류를 요약해서 보여줌
  - 최고 기록은 5개까지 저장 및 출력

## 개발 범위

| 항목 | 설명 |
|--------|--------|
| View 구현 | CustomView 기반 게임 화면, 타이틀 화면 및 종료 화면 |
| UI 이벤트 | TouchEvent 처리 (스와이프 제스처 인식), 버튼 입력 |
| 캐릭터 동작 | 점프/슬라이드 애니메이션, 이동 |
| 장애물 시스템 | 랜덤 장애물 생성, 충돌 감지, GameObject 관리 |
| 점수 및 상태 | 시간 기반 점수 계산, 목숨 관리, 종료 조건 처리 |
| 기록 저장 | SharedPreferences 를 이용한 상위 5개 점수 저장 및 표시 |
| 리소스 | 캐릭터 이미지 2종, 장애물 이미지, 배경 이미지, 효과음 |
| 프레임워크 | 이번 학기 제공된 CustomView 기반 게임 루프 및 GameObject 구조 사용 |

## 예상 게임 실행 흐름
1. 타이틀 화면  
   ㄴ 캐릭터 선택 후 게임 시작  
2. 게임 플레이  
   ㄴ 스와이프 조작으로 장애물 회피, 점수 증가  
   ㄴ 목숨이 0이 되면 게임 종료  
3. 게임 종료 화면  
   ㄴ 플레이 시간, 이동 거리, 캐릭터 표시  
   ㄴ 타이틀 화면으로 복귀

## 예상 게임 화면
![타이틀화면](./resource/타이틀화면.png)
#### 예상 인게임 화면(ex. SUBWAY SURFERS)
![인게임화면](./resource/플레이_화면.jpg)
![종료화면](./resource/종료화면.png)


## 📆 개발 일정 (8주간)

| 주차 | 기간 | 개발 내용 | 진행률 | 비고 |
|------|------|-----------|-----|-----------|
| 1주차 | 4/8 ~ 4/14 | 아이디어 확정, 리소스 수집, GitHub 세팅, 화면 설계 | 70% | 리소스 수집 매우 부족
| 2주차 | 4/15 ~ 4/21 | 타이틀 화면 구현, 캐릭터 선택 기능 추가 | 95% | 캐릭터 스탯 기획 부족
| 3주차 | 4/22 ~ 4/28 | 게임 루프, CustomView, 스와이프 점프/슬라이드 구현 | 100% | 구현 완료
| 4주차 | 4/29 ~ 5/5  | 장애물 랜덤 생성, 연속 방지 로직, 충돌 감지 | 65% | 장애물 종류 부족
| 5주차 | 5/6 ~ 5/12  | 목숨 시스템 구현, 게임 오버 처리 | 100% | 구현 완료
| 6주차 | 5/13 ~ 5/19 | 점수 계산, 기록 저장 기능 (SharedPreferences) | 진행중...
| 7주차 | 5/20 ~ 5/26 | 전체 흐름 통합, UI 개선, 애니메이션 적용 | 진행중...
| 8주차 | 5/27 ~ 6/2  | 테스트, 버그 수정, 최종 README.md 정리 및 제출 | 진행중...

---

##  Git Commit 활동 (GitHub Insights 기반)

| 주차 | 커밋 수 |
|------|--------|
| 1주차 | 0회 |
| 2주차 | 0회 |
| 3주차 | 0회 |
| 4주차 | 0회 |
| 5주차 | 1회 |

> 5주차까지 깃허브에 올리는 것을 잊어, 커밋을 하지 못했습니다. 이후부턴 착실히 하겠습니다.

---

##  목표 변경 사항
1. 1차 계획에서는 `activity_main.xml`을 통한 캐릭터 선택 화면을 사용할 예정이었으나,
  **게임 루프에 통합된 TitleScene으로 UI를 옮기는 방식으로 변경**
   - 이유: 게임이 끝나고, TitleScene으로 변경하는 작업이 있는데, 이를 수행하기 위해선 TitleScene을 따로 만들어 관리하는게 편할 거 같다 판단하였습니다.

2. 1차 계획에서는 가로 게임으로 기획했으나, 구현하면서 **세로 게임으로 수정**
   - 이유 : 게임을 구현하다 보니, 가로로 게임을 제작할 시 플레이어 입장에서 너무 많은 움직임 컨트롤(좌/우 이동 시 끝과 끝으로 이동할 때 수 많은 스와이프 요구..)을 요구해야할 것 같아 세로로 구현했습니다.

---

##  MainScene의 주요 GameObject 구조 및 상호작용
### 전체 구조
```
GameActivity (앱 진입점)
 └── TitleScene       (게임 타이틀 / 캐릭터 선택)
      └── MainScene   (실제 게임 플레이)
           ├── Player         (캐릭터: 이동, 점프, 충돌, 목숨 관리)
           ├── Obstacle       (장애물: 랜덤 생성, 이동, 충돌 대상)
           ├── GameOverScene  (게임 종료 후 점수/버튼 표시)
           └── 추후 추가될 예정..

```

### TitleScene.java
- 캐릭터 선택 화면
- Selcet 버튼으로 남자/여자 캐릭터 선택
- 게임 시작 버튼 → 선택한 캐릭터 정보를 MainScene에 넘김

###  MainScene.java
- 역할: 게임 전체 진행 제어 (스코어 증가, 장애물 생성, 충돌 검사 등)
- 구성:
  - 장애물 생성 타이머, 연속 방지 로직
  - 점수(시간 기반 증가가), 체력 HUD 표시
- 상호작용:
  - Player와 Obstacle의 충돌 여부 판단
  - 목숨이 0이면 GameOverScene 전환

### GameOverScene.java
- 역할 : 게임 종료 시 표시되는 화면
   - 현재 점수, 이동 거리, 선택 캐릭터 정보 표시
- 2개의 버튼
   - Retry : MainScene으로 가서 게임 재시작
   - To Title : TitleScene으로 복귀

###  Player.java
- 역할: 캐릭터 제어 (점프, 슬라이드, 좌우 이동), 충돌 처리, 생명 관리
- 구성:
  - SheetSprite 기반 애니메이션 (4x4 이미지 시트)
  - 상태값 (JUMP, SLIDE, RUN)
  - 부드러운 위치 이동 (targetX, targetY 활용)
- 상호작용:
  - 장애물(Obstacle)과 충돌 시 목숨 감소
  - 터치 입력에 따라 스와이프 동작 처리
- 핵심 코드:
```java
@Override
public boolean onTouchEvent(MotionEvent event) {
    float[] pts = Metrics.fromScreen(event.getX(), event.getY());
    switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
            startX = pts[0];
            startY = pts[1];
            return true;
        case MotionEvent.ACTION_UP:
            float dx = pts[0] - startX;
            float dy = pts[1] - startY;
            if (Math.abs(dx) > Math.abs(dy) && Math.abs(dx) > SWIPE_THRESHOLD) {
                if (dx < 0) player.moveLeft();
                else player.moveRight();
            } else if (Math.abs(dy) > SWIPE_THRESHOLD) {
                if (dy < 0) player.jump();
                else player.slide();
            }
            return true;
    }
    return false;
}
```

###  Obstacle.java
- 역할: 장애물 생성 및 이동, Player와 충돌 시 처리
- 구성:
  - Sprite 상속, 일정 속도로 아래로 이동
  - 화면 밖으로 나가면 자동 제거
- 상호작용:
  - Player와 충돌하면 Player의 목숨 차감 및 본인 제거



---

##  어려웠던 점
- 스와이프 제스처를 4방향으로 정확히 감지하는 것이 까다로웠습니다.
- Player 점프/슬라이드 상태 복귀 로직 타이밍 조절이 까다로웠습니다.
- Scene 구조가 명확히 잡혀서 이후 UI/상태 확장은 쉬웠지만, 초반 구조 설정이 어려웠습니다.

---
