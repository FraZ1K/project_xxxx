-- Полноценная программа на Lua
-- Калькулятор факториала и чисел Фибоначчи

local<const> MAX_ITER = 10

local function factorial(n)
    if n <= 1 then
        return 1
    else
        return n * factorial(n - 1)
    end
end

local function fibonacci(n)
    if n <= 1 then
        return n
    else
        return fibonacci(n - 1) + fibonacci(n - 2)
    end
end

local function processNumber(n)
    local fact = factorial(n)
    local fib = fibonacci(n)
    return fact, fib
end

local results = {}

for i = 1, MAX_ITER do
    local fact, fib = processNumber(i)
    results[i] = {
        number = i,
        factorial = fact,
        fibonacci = fib
    }
end

print("Results:")
for i, data in ipairs(results) do
    print(string.format("n=%d: fact=%d, fib=%d", data.number, data.factorial, data.fibonacci))
end

-- Вывод статистики
print("\nDone!")