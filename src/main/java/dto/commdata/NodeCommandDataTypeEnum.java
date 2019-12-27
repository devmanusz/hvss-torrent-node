package dto.commdata;

public enum NodeCommandDataTypeEnum {
    CLIENT_REGISTER,
    CLIENT_DEREGISTER, // probably won't user
    CLIENT_MODIFY,
    CLIENT_REGISTERED,
    CLIENT_INFO,
    TORRENT_NEW,
    TORRENT_START,
    TORRENT_STOP,
    TORRENT_DELETE,
    TORRENT_SEED,
    TORRENT_INFO,
    TORRENT_INFO_ALL
}
