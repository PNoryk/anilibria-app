package tv.anilibria.plugin.data.restapi

import tv.anilibria.plugin.data.network.NetworkClient

class ApiNetworkClient(networkClient: NetworkClient) : NetworkClient by networkClient