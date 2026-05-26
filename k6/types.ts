export type Sensor = {
    id: string,
    name: string,
    apiKey: string,
    sensorType: SensorType,
    createdAt: string
}

export enum SensorType {
    TEMPERATURE = "TEMPERATURE",
    PRESSURE = "PRESSURE",
    HUMIDITY = "HUMIDITY",
}