
"""
분석
"""
def AnaylizeData(openai,messages):
    chat_completion = openai.chat.completions.create(
        model="gpt-3.5-turbo",
        messages=messages
    )

    result=chat_completion.choices[0].message.content
    return result

###################################################################################################

# -*- coding: utf-8 -*-
import sys

if __name__ == "__main__":

    # ChatGPT Connect
    import os
    import openai
    os.environ.get('OPENAI_API_KEY') is None
    os.environ["OPENAI_API_KEY"] = 'sk-'    # 실행 시 api 를 입력하세요.
    openai.api_key = os.getenv("OPENAI_API_KEY")

    print("***************\n\n module 2 is processing \n\n ***********")

    if len(sys.argv) not in [6, 7]:
        print("인자 전달 개수 이상")
        sys.exit(1)

    temp_file_path = sys.argv[1]
    user_idx = sys.argv[2]
    log_created = sys.argv[3]
    characteristics = sys.argv[4]
    preprocessed_data = sys.argv[5]
    if len(sys.argv) == 7:
        unit = sys.argv[6]
    else:
        unit = None

    if characteristic == "부대 행동":
        messages = [
            {"role": "system", "content": "당신은 주어진 데이터를 분석해야 합니다."},
            {"role": "user", "content": "데이터를 분석하여 해당 부대가 주로 수행한 과업은 무엇인지, \
             각 과업에 소요한 시간은 얼마인지, 무슨 과업을 수행했는지 등을 알려주세요.\
             예시: 청군 1대대-1중대는 최초 전술기동 후 점령 과업을 수행하였습니다.\
             부대가 주로 수행한 과업은 '점령'입니다.       "},
            {"role": "assistant", "content": preprocessed_data+"부대 이름: "+name}
        ]

    result=AnaylizeData(openai, messages)

    # 전처리된 데이터를 작성할 경로
    output_file_path = os.path.join(os.getcwd(), "result.txt")


    # 파일에 데이터 쓰기
    with open(output_file_path, "w", encoding="utf-8") as file:
        file.write(result)

    print(f"Data written to {output_file_path}")