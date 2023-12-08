# AudioGeek<sup>TM</sup> Preferences Agent
#### All your preferences in our hands!

### Environment Variables
* `AGENT_LOG_LEVEL` Possible options: `trace`, `debug`, `info` (default), `warn`, `error`
* `AGENT_SECRET`<span style="color:red">*</span> Secret used to decode Huell Token
* `AGENT_JWT_ALGORITHM` Algorithm used to decode Huell Token. Possible options:

| INPUT                | ALGORITHM |
|----------------------|-----------|
| hmacSHA256           | HS256     |
| hmacSHA384           | HS384     |
| hmacSHA512 (default) | HS512     |

* `AGENT_GENRE_MAX_LENGTH` Specifies max length of genre (default: 100)
* `AGENT_GENRE_CHAR_WHITELIST` Only characters on this list will be approved to be used in genre (by default whitelist is off)
