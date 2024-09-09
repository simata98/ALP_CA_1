import os
import dotenv

dotenv.load_dotenv()

os.environ["OPENAI_API_KEY"] = os.getenv("OPENAI_API_KEY")

from langchain_openai import ChatOpenAI
from langchain_core.messages import HumanMessage, SystemMessage
from langchain_core.output_parsers import JsonOutputParser, StrOutputParser

model = ChatOpenAI(model="gpt-4o-mini")
parser = StrOutputParser()

messages = [
    SystemMessage(content="Translate the following from English into Koean"),
    HumanMessage(content="hi!"),
]

result = model.invoke(messages)
print(parser.invoke(result))
