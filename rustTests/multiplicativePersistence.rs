use std::io;
use std::sync::{Arc, Mutex};
use std::thread;

fn main() {
    println!("What should the multiplicative persistence be?:");

    let persistence: u32 = {
        let mut input = String::new();
        io::stdin()
            .read_line(&mut input)
            .expect("Failed to read input");
        input.trim().parse().expect("Invalid input")
    };

    let big_int = Arc::new(Mutex::new(0u32));
    let steps = Arc::new(Mutex::new(0u32));

    let mut handles = vec![];

    for _ in 0..num_cpus::get() {
        let big_int = Arc::clone(&big_int);
        let steps = Arc::clone(&steps);

        let handle = thread::spawn(move || {
            while *steps.lock().unwrap() < persistence {
                let mut current_number = {
                    let mut big_int = big_int.lock().unwrap();
                    *big_int += 1;
                    *big_int
                };

                if contains_invalid_digits(current_number) {
                    continue;
                }

                let mut current_steps = 0;
                while current_number >= 10 {
                    let mut product = 1;

                    while current_number != 0 {
                        let digit = current_number % 10;
                        product *= digit;
                        current_number /= 10;
                    }

                    current_number = product;
                    current_steps += 1;
                }

                let mut steps = steps.lock().unwrap();
                if current_steps > *steps {
                    *steps = current_steps;
                }
            }
        });

        handles.push(handle);
    }

    for handle in handles {
        handle.join().unwrap();
    }

    let big_int = *big_int.lock().unwrap();
    let steps = *steps.lock().unwrap();

    println!("Number: {}", big_int);
    println!("Steps: {}", steps);
}

fn contains_invalid_digits(number: u32) -> bool {
    let number_string = number.to_string();
    number_string.contains('0') || number_string.contains('5') || number_string.contains('1')
}
