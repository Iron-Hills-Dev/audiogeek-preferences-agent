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

