-- Простая функция
function add(a, b)
    return a + b
end

-- Локальная функция
local function subtract(a, b)
    return a - b
end

-- Функция с метод-синтаксисом
local obj = {}
function obj:method(x)
    return self.value + x
end

-- Функция с vararg
function sum(...)
    local total = 0
    local args = {...}
    for i = 1, #args do
        total = total + args[i]
    end
    return total
end

-- Анонимная функция
local multiply = function(a, b) return a * b end

-- Функция с именованным vararg
function printAll(...args)
    for i = 1, args.n do
        print(args[i])
    end
end

-- Рекурсивная функция
local function factorial(n)
    if n <= 1 then
        return 1
    else
        return n * factorial(n - 1)
    end
end