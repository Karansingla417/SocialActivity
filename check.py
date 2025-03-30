#!/usr/bin/env python3
import argparse
import os
import requests
import json

def analyze_diff(diff_content):
    """
    Send the diff to an LLM API to get an explanation.
    You would replace this with your actual API call.
    """
    api_key = os.environ.get("LLM_API_KEY")

    # Example using Anthropic's Claude API
    headers = {
        "x-api-key": api_key,
        "content-type": "application/json"
    }

    prompt = f"""
    Here's a git diff of code changes. Please explain these changes in detail,
    focusing on:
    1. What functionality was modified
    2. Any potential bugs or issues introduced
    3. Suggestions for improvements

    Git diff:
    ```
    {diff_content}
    ```
    """

    payload = {
        "model": "claude-3-opus-20240229",
        "messages": [{"role": "user", "content": prompt}],
        "max_tokens": 1000
    }

    response = requests.post(
        "https://api.anthropic.com/v1/messages",
        headers=headers,
        json=payload
    )

    result = response.json()
    return result["content"][0]["text"]

def main():
    parser = argparse.ArgumentParser(description='Analyze code diff and generate explanation')
    parser.add_argument('--diff', required=True, help='Path to the diff file')
    parser.add_argument('--output', required=True, help='Path to output the analysis')

    args = parser.parse_args()

    with open(args.diff, 'r') as f:
        diff_content = f.read()

    analysis = analyze_diff(diff_content)

    with open(args.output, 'w') as f:
        f.write(f"# Analysis of Changes\n\n{analysis}")

if __name__ == "__main__":
    main()