# AudioGeek<sup>TM</sup> Preferences Agent
#### All your preferences in our hands!

### Environment Variables
* `AGENT_LOG_LEVEL` Possible options: `trace`, `debug`, `info` (default), `warn`, `error`
* `AGENT_SECRET`<span style="color:red">*</span> Secret used to decode Huell Token

| ALGORITHM | KEY MINIMUM LENGTH |
|-----------|--------------------|
| HS256     | 256 bit            |
| HS384     | 384 bit            |
| HS512     | 512 bit            |

* `AGENT_GENRE_MAX_LENGTH` Specifies max length of genre (default: 100)
* `AGENT_GENRE_CHAR_WHITELIST` Only characters on this list will be approved to be used in genre (by default whitelist is off)
#### Database
This application uses Postgresql database.
* `AGENT_DB_URL`<span style="color:red">*</span> DB url in format `jdbc:postgresql://localhost:5432/dbName`
* `AGENT_DB_USER`<span style="color:red">*</span> DB user
* `AGENT_DB_PASSOWRD`<span style="color:red">*</span> DB password
* `AGENT_DB_POOL_SIZE` max amount of connections with db (default: 5)
* `AGENT_DB_CONNECTION_TIMEOUT` time (in ms) of waiting for db answers
after which timeout is declared (default: 5000)