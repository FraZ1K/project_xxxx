-- Числовой for
for i = 1, 10 do
    print(i)
end

-- Числовой for с шагом
for i = 10, 1, -2 do
    print(i)
end

-- Числовой for с float
for i = 0, 1, 0.1 do
    print(i)
end

-- Обобщённый for (пары)
local t = { a = 1, b = 2, c = 3 }
for k, v in pairs(t) do
    print(k, v)
end

-- Обобщённый for (ipairs)
local arr = { 10, 20, 30, 40 }
for i, v in ipairs(arr) do
    print(i, v)
end

-- Цикл while
local count = 1
while count <= 5 do
    print(count)
    count = count + 1
end

-- Цикл repeat-until
local x = 1
repeat
    print(x)
    x = x + 1
until x > 5

-- Вложенные циклы
for i = 1, 3 do
    for j = 1, 3 do
        print(i * j)
    end
end

-- Break и goto
for i = 1, 10 do
    if i == 5 then
        break
    end
    print(i)
end

-- Метка и goto
::start::
local i = i or 0
i = i + 1
print(i)
if i < 5 then goto start end