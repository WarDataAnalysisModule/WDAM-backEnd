
"""
분석
"""
def AnaylizeData(openai,messages):
    chat_completion = openai.chat.completions.create(
        model="gpt-4o",
        messages=messages
    )

    result=chat_completion.choices[0].message.content
    return result

###################################################################################################

"""
breif: ChatGPT API에 입력할 메시지 작성
param1: 분석 특성, 전처리 데이터, 부대명, 분석 기준 파일명
"""
def CreateMessage(characteristic, preprocessed_data, name, std_config_path):
    messages = []
    if characteristic=="부대 이동 속도 / 위치 변화":
        with open(std_config_path, "r", encoding="utf-8") as file: # 분석 기준
            std=file.read()
        messages = [
            {"role": "system", "content": "당신은 주어진 데이터를 분석해야 한다."},
            {"role": "user", "content": "데이터는 다음 필드로 구성되어 있다."
                                        "simulation time(시간 sec), positionLat(위도), positionLong(경도), positionAlt(고도), speed(이동 속도 km/h)\n"
                                        "당신은 이 데이터를 이용하여 부대의 이동 속도 및 위치에 대해 분석한다.\n"
                                        "simulation time을 이용하여 총 관측 시간을 구하고,"
                                        "첫 번째 행의 데이터를 시작 지점이라 보고 마지막 행의 데이터를 도착 지점으로 볼 때 위도, 경도 ,고도 값을 이용하여 시작 지점에서 도착 지점까지의 거리를 구하여라.\n"
                                        "최고 속도, 최저 속도와 speed가 0인 값을 제외한 이동 중 평균 속도를 구하고 분석 기준을 적용하여 분석하라.\n"
                                        "분석 기준: "+std+"\n"
                                                      "분석 결과의 예시는 다음과 같다. 예시와 같이 html 태그를 이용하여 형식에 맞게 대답해라.\n"
                                                      "해당 부대의 이동 속도 및 위치를 분석한 결과입니다.<br>"
                                                      "{부대명} 부대는 약 50분 동안 1km의 거리를 평균 3.7km/h, 최저 0.5km/h, 최고 10km/h의 속도로 이동하였습니다.<br>"
                                                      "<h1>1. 관측 시간 </h1><br>"
                                                      "총 관측 시간은 약 3000초로 50분에 해당합니다.<br>"
                                                      "<h1>2. 위치</h1><br>"
                                                      "이동 시작 지점: 위도 38.05, 경도 127.16, 고도 103<br>"
                                                      "도착 지점은 위도 38.06, 경도 127.17, 고도 56<br>"
                                                      "수평 거리와 고도 변화를 고려한 총 이동 거리는 직선 거리 상으로 약 1000.07m로, 약 1km입니다.<br>"
                                                      "<h1>3. 속도</h1><br>"
                                                      "평균 이동 속도는 3.7km/h로 평균 속도는 보통입니다. 700초에서 2000초 사이는 10km/h의 빠른 속도로 이동하였습니다. 그러나 이를 제외한 대부분 0.7km/h의 느린 속도로 이동하였습니다.<br>"
             },
            {"role": "assistant", "content": "부대 이름: "+name+"\n"+preprocessed_data}
        ]
    elif characteristic=="인원/장비 수량 변화": # 2
        messages = [
            {"role": "system", "content": "당신은 주어진 데이터를 분석해야 한다."},
            {"role": "user", "content": "이 데이터는 해당 부대의 초기 인원, 장비 , 자원품 수량 정보와 전투를 치른 후 최종 인원, 장비, 자원품 수량 정보와 전투에 대한 정보에 대한 데이터이다.\n"
                                        "초기 인원, 장비 , 자원품 수량 정보와 전투를 치른 후 최종 인원, 장비, 자원품 수량 정보를 비교하여,"
                                        "해당 부대가 인원, 장비, 자원에서 어느 정도의 손실과 변화를 겪었는지 분석하여라.\n"
                                        "요약한 분석 결과를 두괄식으로 답변하고 정확한 인원, 장비, 자원 정보를 세부적으로 보여줘라. 마지막으로는 해당 부대에 어떤 지원이 필요할지 의견을 제시하라.\n"
                                        "다음 예시를 참고하여 답변하라.\n"
                                        "만약 주어진 데이터가 다음과 같다면,\n"
                                        "1. A-1-1 부대 초기 인원, 장비 수량:\n"
                                        "- 인원: 소/중위(소총) 1명, 하/중사(소총) 5명, 병사(소총) 20명, 병사(유탄발사기) 5명, 병사(기관총) 3명\n"
                                        "- 장비 수량: 소총 50개, K201/M203 6개, K-3 3개\n"
                                        "- 자원품 수량: 소총탄 3600개, 기관총탄 3000개, 유탄발사기탄 90개\n\n"
                                        "2. A-1-1 부대 최종 인원, 장비 수량:\n"
                                        "- 인원: 소/중위(소총) 1명, 하/중사(소총) 5명, 병사(소총) 10명, 병사(유탄발사기) 0명, 병사(기관총) 1명\n"
                                        "- 장비 수량: 소총 30개, K201/M203 3개, K-3 3개\n"
                                        "- 자원품 수량: 소총탄 1000개, 기관총탄 1000개, 유탄발사기탄 30개\n\n"
                                        "3. 전투 시간: \n"
                                        "3000~3040초\n\n"
                                        "4. A-1-1과 교전한 부대 목록:\n"
                                        "- B-1-1(id:5)\n"
                                        "- B-1-2(id:6)\n\n"
                                        "분석 결과는 다음과 같아야 한다. 예시와 같이 html 태그를 이용하여 형식에 맞게 대답해라.\n"
                                        "분석 결과, A-1-1 부대는 아래 전투 후 다음과 같은 손실을 얻었습니다.<br>"
                                        "- <b>인원</b>은 병사의 사망율이 높았으며 총 인원은 50% 감소했습니다.<br>"
                                        "- <b>장비</b>는 손실한 소총 수량이 크지만, K-3 장비는 손실이 없습니다.<br>"
                                        "- <b>자원</b>은 대부분 50% 이상 감소하였으며, 소총탄의 손실이 가장 큽니다.<br>"
                                        "<h1>1. 전투 정보 </h1><br>"
                                        "- A-1-1 부대가 전투에 참여한 시간: 3000초에서 3040초로 약 40초 동안입니다.<br>"
                                        "- B-1-1(id:5), B-1-2(id:6) 부대와 전투하였습니다.<br>"
                                        "<h1>2. 인원 변화</h1><br>"
                                        "- 병사(소총) 10명, 병사(유탄발사기) 5명, 병사(기관총) 2명 손실<br>"
                                        "- 총 34명에서 17명으로 17명(50%) 감소<br>"
                                        "<h1>3. 장비 변화</h1><br>"
                                        "- 소총 20개, K201/M203 3개 손실<br>"
                                        "- 현재 수량: 소총 30개, K201/M203 3개, K-3 3개<br>"
                                        "<h1>4. 자원 변화</h1><br>"
                                        "- 소총탄 2800개, 기관총탄 2000개, 유탄발사기탄 60개 손실<br>"
                                        "- 현재 수량: 소총탄 1000개, 기관총탄 1000개, 유탄발사기탄 30개<br>"
                                        "세부 정보는 다음과 같습니다.\n\n"
                                        "<h1>A-1-1 부대 초기 인원, 장비 수량</h1><br>"
                                        "- <b>인원:</b> 소/중위(소총) 1명, 하/중사(소총) 5명, 병사(소총) 20명, 병사(유탄발사기) 5명, 병사(기관총) 3명<br>"
                                        "- <b>장비 수량:</b> 소총 50개, K201/M203 6개, K-3 3개<br>"
                                        "- <b>자원품 수량:</b> 소총탄 3600개, 기관총탄 3000개, 유탄발사기탄 90개<br>"
                                        "<h1>A-1-1 부대 최종 인원, 장비 수량</h1>"
                                        "- <b>인원:</b> 소/중위(소총) 1명, 하/중사(소총) 5명, 병사(소총) 10명, 병사(유탄발사기) 0명, 병사(기관총) 1명<br>"
                                        "- <b>장비 수량:</b> 소총 30개, K201/M203 3개, K-3 3개<br>"
                                        "- <b>자원품 수량:</b> 소총탄 1000개, 기관총탄 1000개, 유탄발사기탄 30개<br>"
                                        "결론적으로 A-1-1 부대는 상당한 손실을 입었으며, <b>병사 증원과 소총탄 충전 및 장비 보충</b>이 시급해보입니다.<br>"
             },
            {"role": "assistant", "content": "부대 이름: "+name+"\n"+preprocessed_data}
        ]
    elif characteristic == "부대의 전투력": # 3
        messages = [
            {"role": "system", "content": "당신은 주어진 데이터를 분석해야 합니다."},
            {"role": "user", "content": "데이터를 분석하여 부대의 전투력이 변화되기 시작한 시각이 언제부터 언제인지, \
             최초 power와 마지막으로 기록된 power를 말해주고, 이들을 비교해서 전투력이 얼마나 감소했는지를 알려주세요.\
             최초 power와 마지막으로 기록된 power 사이의 증가량에서 부대의 전투력이 변화된 시각의 증가량을 나눠서 0.1보다 크다면 급격하게 감소되었다고\
             아래와 같은 형식처럼 분석한 내용을 html으로만 알려주세요. \
             예시: \
             <body> \
                <h1>청군 1대대-1중대 전투력 변화 보고</h1> \
                <h2>전투력의 완만한 감소</h2> \
                <p>청군 1대대-1중대의 전투력은 1040부터 8940까지의 시간 동안 완만하게 감소하였습니다.</p> \
                <h2>전투력 변화 시각 및 기록</h2> \
                <p>전투력이 변화된 시각은 1040부터 8940이며, 처음 기록된 전투력은 100이었고 마지막으로 기록된 전투력은 60으로, 초기 대비 40% 감소하였습니다.</p> \
                <h2>전투력 감소 분석</h2> \
                <p>1040부터 8940까지의 시간 동안 전투력이 100에서 60으로 감소한 것은 부대의 전투력이 완만하게 감소되었음을 의미합니다.</p> \
            </body>"},
            {"role": "assistant", "content": preprocessed_data+"부대 이름: "+name}
        ]
    elif characteristic == "부대의 피해 상황": # 4
        messages = messages = [
            {"role": "system", "content": "당신은 주어진 데이터를 분석해야 합니다."},
            {"role": "user", "content": "데이터를 분석하여 해당 부대에서 바로 이전 데이터와 비교해서 power가 가장 많이 줄어든 시간대와 행동이름을 알려주고 \
             부대의 주요 BehaviorName의 종류와 해당 BehaviorName이 포함된 데이터만 비율을 알려주세요.\
             예시와 같이 제목, 두괄식 문장, 문장형식을 이용하여 html으로 표현해주세요.\
             예시: \
             <body> \
                <h1>청군 4중대-2기관총분대 손실 보고</h1> \
                <h2>근접전투로 인한 주요 손실</h2> \
                <p>청군 4중대-2기관총분대는 2:40에 근접전투로 인해 가장 큰 손실을 입었습니다.</p> \
                <h2>주요 손실 유형: 직접사격</h2> \
                <p>청군의 주요 손실 유형은 직접사격으로, 피해의 50%가 이로 인해 발생하였습니다.</p> \
                <h2>부대의 피해 상태</h2> \
                <p>결과적으로, 청군 4중대-2기관총분대는 직접사격으로 인해 부대 대부분이 피해를 입었습니다.</p> \
            </body>"},
            {"role": "assistant", "content": preprocessed_data+"부대 이름: "+name}
        ]
    elif characteristic == "부대 행동": # 5
        messages = [
            {"role": "system", "content": "당신은 주어진 데이터를 분석해야 합니다."},
            {"role": "user", "content": "데이터를 분석하여 해당 부대가 주로 수행한 과업은 무엇인지, 각 과업에 소요한 시간은 얼마인지, 무슨 과업을 수행했는지 등을 알려주세요.\n"
                                        "다음 예시를 참고하여 html 태그를 이용하여 아래 형식에 맞춰 알려주세요.\n\n"
                                        "만약 주어진 데이터가 다음과 같다면,\n"
                                        "총 관측 시간: 1000\n"
                                        "1. 과업명: 점령\n"
                                        "- 시작 시각: 20\n"
                                        "- 종료 시각: 100\n"
                                        "- 소요 시간: 80\n"
                                        "2. 과업명: 공격\n"
                                        "- 시작 시각: 120\n"
                                        "- 종료 시각: 200\n"
                                        "- 소요 시간: 80\n"
                                        "3. 과업명: 점령\n"
                                        "- 시작 시각: 400\n"
                                        "- 종료 시각: 700\n"
                                        "- 소요 시간: 300\n"
                                        "결과는 다음과 같아야 합니다.\n"
                                        "<b>{부대명}부대</b>가 총 관측 시간 1000초 동안 주로 수행한 과업은 <b>'점령'</b>이며 각 과업별 세부 사항은 다음과 같습니다.<br>"
                                        "<h1>총 관측 시간: 1000</h1><br>"
                                        "<h2>1. 과업명: 점령</h2><br>"
                                        "- 시작 시각: 20<br>"
                                        "- 종료 시각: 100<br>"
                                        "- 소요 시간: 80<br>"
                                        "<h2>2. 과업명: 공격</h2><br>"
                                        "- 시작 시각: 120<br>"
                                        "- 종료 시각: 200<br>"
                                        "- 소요 시간: 80<br>"
                                        "<h2>3. 과업명: 점령</h2><br>"
                                        "- 시작 시각: 400<br>"
                                        "- 종료 시각: 700<br>"
                                        "- 소요 시간: 300<br>"
             },
            {"role": "assistant", "content": "부대 이름: "+ name + preprocessed_data}
        ]
    elif  characteristic == "개체 탐지": #6
        messages = [
            {"role": "system", "content": "당신은 주어진 데이터를 분석해야 합니다."},
            {"role": "user", "content": "데이터를 분석하여 해당 부대가 탐지한 개체의 최초 탐지 시점과 거리, 최종 탐지 시점과 거리, 전투력이 어떻게 변화했는지, 피해 상태를 알려주세요. \
                        html 태그도 함께 출력해주세요.\
                        Detected_Entity_ID는 탐지된 개체의 ID이다. \
                        DamageState는 NoDamage / SlightDamage / ModerateDamage / Destroyed 총 4가지이다.\
                        NoDamage는 아무런 피해가 없는 상태, SlightDamage는 약간의 피해를 입은 상태, ModerateDamage는 어느 정도 피해를 입은 상태, Destroyed는 완전히 파괴된 상태이다.\
                        탐지된 모든 개체에 대해 분석해주세요. \
             아래의 예시의 형식처럼 알려주세요.\
             예시: \
                해당 부대가 탐지한 개체들에 대한 분석 결과입니다. \
                <h1>1. 탐지된 개체들에 대한 정보</h1> \
                <h3>개체 7:</h3> 8280초에 B-1-1-Unit2로부터 973.46250m 떨어진 거리에서 최초로 탐지 되었으며, 8880초에 최종 탐지되어 B-1-1-Unit2(부대 이름)로부터 거리는 193.28714m입니다. \
                <h3>개체 8:</h3> 8280초에 B-1-1-Unit2로부터 983.23653m 떨어진 거리에서 최초로 탐지 되었으며, 8910초에 최종 탐지되어 B-1-1-Unit2(부대 이름)로부터 거리는 388.13182m입니다.  \
                <h3>개체 9:</h3> 8220초에 B-1-1-Unit2로부터 974.23594m 떨어진 거리에서 최초로 탐지 되었으며, 8880초에 최종 탐지되어 B-1-1-Unit2(부대 이름)로부터 거리는 623.63980m입니다.  \
                <h3>개체 11:</h3> 8430초에 B-1-1-Unit2로부터 966.64618m 떨어진 거리에서 최초로 탐지 되었으며, 8880초에 최종 탐지되어 청B-1-1-Unit2(부대 이름)로부터 거리는 429.12943m입니다. \
                \
                <br> <h1>2. 전투력 변화 </h1> \
                B-1-1-Unit1부대가 처음 개체를 탐지한 시점에 기록된 전투력은 100(Power)이었고, 마지막으로 기록된 전투력은 27.27(Power)으로, 약 30% 감소하였습니다. \
                \
                <br> <h1>3. 피해 상태 변화 </h1> \
                B-1-1-Unit1부대가 처음 개체를 탐지한 시점에는 아무런 피해가 없었고(NoDamage), 마지막으로 기록된 상태는 어느 정도 피해를 입은(ModerateDamage) 상태입니다. "},
            {"role": "assistant", "content": preprocessed_data+"부대 이름: "+name}
        ]
    elif characteristic == "부대 정보": #7
        messages = [
            {"role": "system", "content": "당신은 주어진 데이터를 분석해야 합니다."},
            {"role": "user", "content": "데이터를 분석하여 각 부대에 대하여 초기 상태와 초기 인원, 초기 장비, 초기 보급품의 개수를 알려주세요.  \
                    html 태그도 함께 출력해주세요.\
                    초기 상태는 NoDamage / SlightDamage / ModerateDamage / Destroyed 총 4가지이다.\
                    NoDamage는 아무런 피해가 없는 상태, SlightDamage는 약간의 피해를 입은 상태, ModerateDamage는 어느 정도 피해를 입은 상태, Destroyed는 완전히 파괴된 상태이다.\
             다음 예시를 참고하여 형식에 맞춰 알려주세요. \
             예시: \
             분석 가능한 모든 부대들의 초기 정보입니다. \
             <h1>부대 이름: B-1-1-Unit1(청군 1대대-1중대)</h1>\
                - 초기 상태: 아무런 피해 없음(NoDamage) <br>\
                - 초기 인원: 소/중위(소총) 1명, 하/중사(소총) 4명, 병(소총) 19명, 병(유탄발사기) 6명, 병(기관총) 3명 <br> \
                - 초기 장비: 개인/공용화기(소총) 24개, 개인/공용화기(K201/M203) 6개, 개인/공용화기(K-3) 3개 <br> \
                - 초기 보급품: 소총탄 3600발, 기관총탄 3000발, 유탄발사기탄 90발"},
            {"role": "assistant", "content": preprocessed_data}
        ]
    return messages

###################################################################################################

"""
breif: 시각자료 생성 시 ChatGPT API에 입력할 메시지 작성
param1: 전처리 데이터와 저장할 이미지 파일 명
"""
def CreateImage(characteristic, preprocessed_data, img_name):
    messages = []
    if characteristic == "부대 이동 속도 / 위치 변화":
        messages = [
            {"role": "system", "content": "당신은 목적에 맞는 파이썬 코드를 작성해야 합니다."},
            {"role": "user", "content":
                "데이터는 다음 필드로 구성되어 있습니다."
                "simulation time(관측시간 sec), positionLat(위도), positionLong(경도), positionAlt(고도), speed(이동 속도 km/h)\n"
                "주어진 데이터에서 simulation time에 따른 speed의 변화를 그래프로 그리고 이미지 파일을 저장하세요."
                "이때 이미지 파일명은 "+img_name+"이어야 합니다."
                                        "그래프의 제목은 'Speed changes over time'이고, y축은 speed, x축은 simulation time이어야 합니다."
                                        "어떤 부연 설명 없이 바로 실행할 수 있도록 오직 코드만 작성해주세요."
                                        "예를 들어 ```python 같은 불필요한 표시 없이"
                                        "import matplotlib.pyplot as plt로 시작하여 plt.savefig('"+img_name+"')처럼 이미지를 저장하세요."},
            {"role": "assistant", "content": preprocessed_data}
        ]
    elif characteristic == "부대의 피해 상황":
        messages = [
            {"role": "system", "content": "당신은 목적에 맞는 파이썬 코드를 작성해야 합니다."},
            {"role": "user", "content":
                "주어진 데이터에서 simulation time에 따른 power의 변화를 그래프로 그리고 이미지 파일을 저장하세요."
                "이때 이미지 파일명은 "+img_name+"이어야 합니다."
                                        "그래프의 제목은 'Power changes over time'이고, y축은 power, x축은 simulation time이어야 합니다."
                                        "어떤 부연 설명 없이 바로 실행할 수 있도록 오직 코드만 작성해주세요."
                                        "예를 들어 ```python 같은 불필요한 표시 없이"
                                        "import matplotlib.pyplot as plt로 시작하여 plt.savefig('"+img_name+"')처럼 이미지를 저장하세요."},
            {"role": "assistant", "content": preprocessed_data}
        ]
    return messages


# -*- coding: utf-8 -*-
import sys

if __name__ == "__main__":

    print("***************\n\n module 2 is processing \n\n***************")

    # ChatGPT Connect
    import os
    import openai
    os.environ.get('OPENAI_API_KEY') is None
    os.environ["OPENAI_API_KEY"] = 'sk-'    # 실행 시 api 를 입력하세요.
    openai.api_key = os.getenv("OPENAI_API_KEY")

    if len(sys.argv) not in [6, 7]:
        print("인자 전달 개수 이상")
        sys.exit(1)

    temp_file_path = sys.argv[0]
    user_idx = sys.argv[1]
    log_created = sys.argv[2]
    characteristic = sys.argv[3]
    preprocessed_data = sys.argv[4]
    std_config_path = sys.argv[5]
    if len(sys.argv) == 7:
        name = sys.argv[6]
    else:
        name = None

    print("std_config_path: ", std_config_path)

    if characteristic=="부대 이동 속도 / 위치 변화":
        messages=CreateMessage(characteristic, preprocessed_data, name, std_config_path)
    elif characteristic == "인원/장비 수량 변화":
        messages = CreateMessage(characteristic, preprocessed_data, name, std_config_path)
    elif characteristic == "부대의 전투력":
        messages = CreateMessage(characteristic, preprocessed_data, name, std_config_path)
    elif characteristic == "부대의 피해 상황":
        messages = CreateMessage(characteristic, preprocessed_data, name, std_config_path)
    elif characteristic == "부대 행동":
        messages = CreateMessage(characteristic, preprocessed_data, name, std_config_path)
    elif characteristic == "개체 탐지":
        messages = CreateMessage(characteristic, preprocessed_data, name, std_config_path)
    elif characteristic == "부대 정보":
        messages = CreateMessage(characteristic, preprocessed_data, name, std_config_path)
    else:
        messages=[]

    # 분석 실행
    result=AnaylizeData(openai, messages)

    # 분석 결과를 작성할 경로
    output_file_path = os.path.join(os.getcwd(), "result.txt")
    # 파일에 분석 결과 쓰기
    with open(output_file_path, "w", encoding="utf-8") as file:
        file.write(result)
    print(f"Data written to {output_file_path}")

    # print(result)

    # 시각 자료 생성
    img_name="" # 시각 자료 파일명
    img_success=0 # 이미지 생성 여부
    if characteristic=="부대 이동 속도 / 위치 변화" or characteristic=="부대의 피해 상황":

        # 파일명 중복을 피하기 위해 생성 시간으로 파일명 저장
        from datetime import datetime
        img_name=str(datetime.now())+".png"
        img_name=img_name.replace(":","_")
        # 그래프 그리는 코드 생성
        messages=CreateImage(characteristic, preprocessed_data, img_name)
        code=AnaylizeData(openai, messages)
        code = code.replace("```python", " ").replace("```", " ")

        try:
            # 코드 실행
            exec(code)
            print("python module is making graph for ", characteristic)
            img_success=1 # 이미지 생성 완료
        except Exception as e:
            # 코드 실행 실패 시 이미지 파일명 초기화
            img_name=""
            print("python module error while making graph for ", characteristic)
    else: # 다른 특성인 경우 이미지 파일 x
        img_name=""

    url="" # s3에 업로드된 파일에 접근할 수 있는 경로
    if img_success==1:   # 이미지 생성된 경우
        import boto3
        # 실행 시 키 입력
        AWS_ACCESS_KEY_ID=""
        AWS_SECRET_ACCESS_KEY = ""
        AWS_DEFAULT_REGION = ""

        client = boto3.client('s3',
                              aws_access_key_id=AWS_ACCESS_KEY_ID,
                              aws_secret_access_key=AWS_SECRET_ACCESS_KEY,
                              region_name=AWS_DEFAULT_REGION
                              )

        bucket = ''           #버켓 주소, 실행 시 입력
        key = img_name # s3에 저장될 이름

        # s3 버킷에 이미지 업로드
        client.upload_file(
            img_name, bucket, key,
            ExtraArgs={'ContentType': 'image/png'}  # Content-Type 설정
        )

        if img_name!="": # 시각 자료를 성공적으로 생성한 경우 url 생성
            url="실행 시 버킷 url 입력"+img_name
        print("url: ",url)

        # s3에 파일 업로드 후 시스템에서 제거
        import os
        try:
            os.remove(img_name)
            print(f"{img_name} File removed.")
        except FileNotFoundError:
            print(f"Cannot find File: {img_name}")
        except Exception as e:
            print(f"Error while removing file: {e}")



    # img_url에 시각자료 경로 저장
    # 생성되지 않은 경우에는
    with open("img_url.txt","w",encoding="utf-8") as file:
        file.write(url)