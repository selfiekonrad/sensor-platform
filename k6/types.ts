export type Sensor = {
    id: String,
    name: String,
    apiKey: string,
    sensorType: SensorType,
    createdAt: String
}

export enum SensorType {
    TEMPERATURE = "TEMPERATURE",
    PRESSURE = "PRESSURE",
    HUMIDITY = "HUMIDITY",
}