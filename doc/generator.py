from pymysql import Connect, connect
import argparse
import string
import random

STATES = [
    (False, "FIRMWARE", "PENDING", ""),
    (True, "FIRMWARE", "PENDING", ""),
    (True, "FIRMWARE", "PROCESSING", ""),
    (True, "CONFIGURATION", "PENDING", ""),
    (True, "CONFIGURATION", "PROCESSING", ""),
    (True, "CONFIGURATION", "FINISHED", ""),
    (False, "CONFIGURATION", "FINISHED", ""),
    (False, "FIRMWARE", "PROCESSING", ""),
    (True, "FIRMWARE", "FAILED", "A firmware error happened !"),
    (True, "CONFIGURATION", "FAILED", "A configuration error happened")
]

PROGRAM_DESCRIPTION = """
    A program to populate the BRS database with chargepoints and status !
    NOTE : The program requires you to have configurations created in the database.
"""


def connect_to_db(host: str,
                  user: str,
                  password: str,
                  database: str,
                  port: int) -> Connect:
    """
    Connects to the database.

    Returns:
        Connect: The established connection to the database.
    """
    return connect(
        host=host,
        user=user,
        password=password,
        database=database,
        port=port
    )


def populate_status_with_inserts(connection: Connect, number_of_elements: int) -> list[int]:
    """
    Inserts into the database, n status (given by the parameter number_of_elements).

    Args:
        connection (Connect): The current connection to the database.
        number_of_elements (int): The number of wanted status.

    Returns:
        list[int]: The list of id of the newly created status.
    """
    if (number_of_elements <= 0):
        return []
    id_list = []
    with connection.cursor() as cursor:     
        for i in range(number_of_elements):
            query = "INSERT INTO status (state, step, step_status, error) VALUES (%s, %s, %s, %s)"
            cursor.execute(query, STATES[i % len(STATES)])
            id_list.append(cursor.lastrowid)
    connection.commit()
    return id_list


def populate_chargepoint_with_inserts(connection: Connect, 
                                      number_of_elements: int,
                                      status_ids: list[int],
                                      configuration_ids: list[int]):
    """
    Inserts into the database, chargepoints according to the created status and.
    
    Args:
        connection (Connect): The current connection to the database.
        number_of_elements (int): The number of wanted chargepoints.
        status_ids (list[int]): The list of the ids of the created status.
        configuration_ids (list[id]): The list of the ids of the existing configuration.
    """
    if (number_of_elements <= 0):
        return
    with connection.cursor() as cursor:
        for i in range(number_of_elements):
            randomString = ''.join(random.choices(string.ascii_uppercase + string.digits, k=10))
            server_address = "www." + randomString + ".com"
            query = """INSERT INTO chargepoint 
            (serial_number_chargepoint, type, constructor, client_id, server_address, id_status, id_configuration)
            values (%s, \"NG90*\", \"Alfen BV\", %s, %s, %s, %s)
            """
            cursor.execute(query, (randomString, randomString, server_address, status_ids[i], configuration_ids[i % len(configuration_ids)]))
    connection.commit()


def fetch_existing_configurations(connection: Connect) -> list[int]:
    """
    Retrieves all the existing configuration from the database.

    Args:
        connection (Connect): The current connection to the database.

    Returns:
        list[int]: All the id of the existing configuration.
    """
    id_list = []
    with connection.cursor() as cursor:
        query = "SELECT id_configuration FROM configuration"
        cursor.execute(query)
        results = cursor.fetchall()
        for result in results:
            id_list.append(result[0])
    return id_list
     
   
def main():
    """
    This program is used to populate the database with chargepoints and status.\n
    This program needs configurations to be created beforehand and pymysql installed.
    """
    parser = argparse.ArgumentParser(description=PROGRAM_DESCRIPTION)
    parser.add_argument("--host", help="The database host (REQUIRED).", type=str, required=True)
    parser.add_argument("--user", help="The database user (REQUIRED).", type=str, required=True)
    parser.add_argument("--password", help="The database password (REQUIRED).", type=str, required=True)
    parser.add_argument("--database", help="The database name (REQUIRED).", type=str, required=True)
    parser.add_argument("--port", help="The database port (REQUIRED).", type=int, required=True)
    parser.add_argument("-n", "--number", help="The number of chargepoint (DEFAULT : 10).", type=int, default=10)
    args = parser.parse_args()
    host = args.host
    user = args.user
    password = args.password
    database = args.database
    port = args.port
    number = args.number
    connection = connect_to_db(host=host,
                               user=user,
                               password=password,
                               database=database,
                               port=port)
    ids = populate_status_with_inserts(connection=connection, number_of_elements=number)
    configuration_ids = fetch_existing_configurations(connection=connection)
    populate_chargepoint_with_inserts(connection=connection,
                                      number_of_elements=number,
                                      status_ids=ids,
                                      configuration_ids=configuration_ids)


if __name__ == "__main__":
    main()
