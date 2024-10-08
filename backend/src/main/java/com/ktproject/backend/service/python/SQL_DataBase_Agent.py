import os
import sys
from langchain_community.utilities.sql_database import SQLDatabase
from dotenv import load_dotenv
from langchain_openai import ChatOpenAI
from langchain_community.agent_toolkits.sql.toolkit import SQLDatabaseToolkit
from langchain import hub
from langgraph.prebuilt import create_react_agent

def get_user_settings():
  """DB, API 세팅을 환경설정 합니다."""
  load_dotenv()
  os.environ["OPENAI_API_KEY"] = os.getenv("OPENAI_API_KEY")
  os.environ["LANGCHAIN_API_KEY"] = os.getenv("LANGSMITH_API_KEY")
  return {
    "POSTGRES_USER": os.getenv("POSTGRES_USER"),
    "POSTGRES_PASSWORD": os.getenv("POSTGRES_PASSWORD"),
    "DB_URL": os.getenv("DB_URL"),
  }

def get_engine_for_postgres_db(settings, db_name):
  """Generate SQLDatabase object from DB settings."""
  db_url = f"postgresql+psycopg2://{settings['POSTGRES_USER']}:{settings['POSTGRES_PASSWORD']}@{settings['DB_URL']}/{db_name}"
  try:
    return SQLDatabase.from_uri(db_url)
  except Exception as e:
    raise ConnectionError(f"Failed to connect to database: {e}")

# Function to setup the language model and agent executor
def setup_agent(db):
  """Initialize the agent with the database and return the agent executor."""
  llm = ChatOpenAI(model="gpt-4o")  # Initialize the LLM
  toolkit = SQLDatabaseToolkit(db=db, llm=llm)  # Create toolkit for the SQL agent
  
  # Load system prompt template from LangChain Hub
  prompt_template = hub.pull("langchain-ai/sql-agent-system-prompt")
  
  if not prompt_template or len(prompt_template.messages) != 1:
      raise ValueError("Invalid prompt template format")

  # Format the system message for the agent
  system_message = prompt_template.format(dialect="PostgreSQL", top_k=5)

  # Create and return the agent executor
  return create_react_agent(llm, toolkit.get_tools(), state_modifier=system_message)

# Function to execute a query and stream results
def run_agent_query(agent_executor, query):
  """Execute the agent query and stream the results."""
  try:
    events = agent_executor.stream(
        {'messages': [('user', query)]}, stream_mode="values",
    )
    for event in events:
      event['messages'][-1].pretty_print()  # Print streamed results
  except Exception as e:
    print(f"Error during query execution: {e}")

# Main execution flow
if __name__ == "__main__":
    if len(sys.argv) > 1:
      db_name = sys.argv[1]
      input_query = sys.argv[2]
      
    try:
        settings = get_user_settings()  # Load environment settings
        db = get_engine_for_postgres_db(settings, db_name)  # Connect to the database
        agent_executor = setup_agent(db)  # Setup agent with the toolkit


        run_agent_query(agent_executor, input_query)  # Run the agent query
    except Exception as e:
        print(f"An error occurred: {e}")
