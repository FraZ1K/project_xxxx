-- Пустая таблица
local a = {}

-- Таблица с полями
local person = {
    name = "John",
    age = 30,
    city = "New York"
}

-- Таблица со смешанными полями
local mixed = {
    "value1",
    "value2",
    key1 = 100,
    key2 = 200,
    [1] = "index1",
    ["computed"] = 42,
    [10 + 5] = "computed key"
}

-- Вложенная таблица
local nested = {
    a = 1,
    b = { x = 10, y = 20 },
    c = { 1, 2, 3 }
}

-- Таблица с выражением в ключе
local t = {
    [true] = "boolean key",
    [false] = "another boolean",
    [nil] = "ignored"
}

-- Таблица как массив
local colors = { "red", "green", "blue", "yellow" }

-- Таблица с функцией
local obj = {
    value = 42,
    getValue = function(self) return self.value end
}