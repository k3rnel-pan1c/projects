use std::fs::{File, OpenOptions};
use std::io::{BufRead, BufReader, Write};
use std::path::Path;
use std::time::{Duration, Instant};

fn main() {
    let max_value: i64 = 1_000_000_000_000_000;
    let file_path = "filtered_numbers.txt".to_string();
    let mut start_time = Instant::now();

    // Find the largest number in the existing file
    let largest_number = get_largest_number_from_file(&file_path);

    let mut file = OpenOptions::new()
        .append(true)
        .create(true)
        .open(&file_path)
        .unwrap();

    let mut current_number = largest_number + 1;
    let mut buffer = Vec::new(); // Buffer for storing numbers before writing to file

    loop {
        if current_number <= max_value {
            if is_ascending_order(current_number)
                && !contains_digits(current_number, &[0, 1, 5])
                && count_occurrences(current_number, 3) <= 1
            {
                buffer.push(current_number);
            }

            let elapsed_time = start_time.elapsed().as_secs();
            if elapsed_time >= 60 {
                println!("Current number: {}", current_number);
                // Reset start time
                start_time = Instant::now();
            }

            // Write the buffer to file in batches of 1000000 numbers
            if buffer.len() >= 1000000 {
                write_numbers_to_file(&mut file, &buffer);
                buffer.clear();
            }

            current_number += 1;
        } else {
            break;
        }
    }

    // Write any remaining numbers in the buffer to file
    if !buffer.is_empty() {
        write_numbers_to_file(&mut file, &buffer);
    }

    println!("Numbers have been written to {}", file_path);
}

fn is_ascending_order(number: i64) -> bool {
    let mut prev_digit = 9;
    let mut n = number;

    while n > 0 {
        let digit = n % 10;
        if digit > prev_digit {
            return false;
        }
        prev_digit = digit;
        n /= 10;
    }

    true
}

fn contains_digits(number: i64, digits: &[i64]) -> bool {
    let mut n = number;

    while n > 0 {
        let digit = n % 10;
        let digit_mask = 1 << digit;

        for &d in digits {
            if digit_mask & (1 << d) != 0 {
                return true;
            }
        }

        n /= 10;
    }

    false
}


fn count_occurrences(number: i64, digit: i64) -> usize {
    let mut n = number;
    let mut count = 0;

    while n > 0 {
        let d = n % 10;
        if d == digit {
            count += 1;
        }
        n /= 10;
    }

    count
}

fn get_largest_number_from_file(file_path: &str) -> i64 {
    if Path::new(file_path).exists() {
        let file = File::open(file_path).unwrap();
        let reader = BufReader::new(file);

        let mut largest_number = 0;

        for line in reader.lines() {
            if let Ok(number) = line.unwrap().parse::<i64>() {
                if number > largest_number {
                    largest_number = number;
                }
            }
        }

        largest_number
    } else {
        0
    }
}

fn write_numbers_to_file(file: &mut File, numbers: &[i64]) {
    let numbers_str: Vec<String> = numbers.iter().map(|&num| num.to_string()).collect();
    let joined_str = numbers_str.join("\n");
    writeln!(file, "{}", joined_str).unwrap();
}
